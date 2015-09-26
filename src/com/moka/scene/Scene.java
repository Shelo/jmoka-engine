package com.moka.scene;

import com.moka.components.Camera;
import com.moka.components.Sprite;
import com.moka.graphics.Shader;
import com.moka.prefabs.OpingPrefabReader;
import com.moka.prefabs.PrefabReader;
import com.moka.scene.entity.Entity;
import com.moka.graphics.Texture;
import com.moka.utils.JMokaException;

import java.util.ArrayList;
import java.util.List;

/**
 * The scene is the element of your game that will actually show something.
 */
public abstract class Scene
{
    private Context context;
    private ArrayList<Integer> layers = new ArrayList<>();
    private ArrayList<Entity> entities = new ArrayList<>();
    private boolean created;

    /**
     * Called the first time the scene is called to play it self.
     */
    public abstract void onCreate();

    /**
     * Called when the game decides that it will show another scene, but not totally exiting the game nor
     * destroying this particular scene.
     */
    public abstract void onLeave();

    /**
     * Called when the game shows this scene the first time or the scene is shown and it wasn't destroyed.
     */
    public abstract void onResume();

    /**
     * Called when the game either stops or it decides that this scene should be destroyed.
     */
    public abstract void onExit();

    /**
     * Updates every entity in the scene.
     */
    public final void update()
    {
        for (int i = entities.size() - 1; i >= 0; i--)
        {
            entities.get(i).update();
        }
    }

    /**
     * The load method is called whenever the scene needs to show up. This will decide if it
     * needs to be created or resumed.
     */
    public void load(Context context)
    {
        if (created)
        {
            onResume();
        }
        else
        {
            this.context = context;

            onCreate();

            // create every entity.
            for (Entity entity : entities)
                entity.create();

            created = true;
        }
    }

    public final void render(Shader shader)
    {
        for (Entity entity : entities)
            if (entity.hasSprite() && entity.getSprite().isEnabled())
                entity.getSprite().render(shader);
    }

    public void postUpdate()
    {
        for (Entity entity : entities)
        {
            entity.postUpdate();
        }
    }

    public void clean()
    {
        for (int i = entities.size() - 1; i >= 0; i--)
        {
            if (entities.get(i).isDestroyed())
            {
                entities.get(i).onDestroy();
                entities.remove(i);
            }
        }
    }

    public void hardReset()
    {
        entities.clear();
    }

    /**
     * Constructs a new {@link Entity} and add it to the hierarchy.
     *
     * @return the new {@link Entity}.
     */
    public final Entity newEntity(String name, int layer)
    {
        // create and add the getEntity to the game.
        Entity entity = new Entity(name, this);
        addEntity(entity, layer);
        return entity;
    }

    /**
     * Gets all entities from a group.
     *
     * @return all entities from that group as an array list or null if the list is empty.
     */
    public List<Entity> getEntitiesFromGroup(String group)
    {
        ArrayList<Entity> groupEntities = new ArrayList<>();

        for (Entity entity : entities)
            if (entity.belongsTo(group))
                groupEntities.add(entity);

        return groupEntities.isEmpty()? null : groupEntities;
    }

    /**
     * Finds an Entity on the game. This method should always return something that is not null,
     * otherwise it will throw an exception.
     *
     * @param name getEntity's name.
     * @return the entity if found.
     */
    public final Entity findEntity(String name)
    {
        if (name == null)
            throw new JMokaException("The name cannot be null.");

        for (Entity entity : entities)
        {
            if (entity.getName().equals(name))
            {
                return entity;
            }
        }

        throw new JMokaException("There's no entity with name " + name + ".");
    }

    /**
     * Adds an getEntity to a given layer.
     *
     * @param entity the getEntity to be added.
     * @param layer  the layer at which the context will add the getEntity.
     * @return the same getEntity given.
     */
    public final Entity addEntity(Entity entity, int layer)
    {
        if (layer == layers.size())
            layers.add(entities.size());
        else if (layer > layers.size())
            throw new JMokaException("Cannot skip layer values.");

        int ptr = layers.get(layer);
        entities.add(ptr, entity);

        for (int i = layer + 1; i < layers.size(); i++)
            layers.set(i, layers.get(i) + 1);

        return entity;
    }

    /**
     * Creates a new {@link Entity} with a camera on it.
     *
     * @param name      name for the getEntity.
     * @param main      sets this camera as the main one.
     * @return          the entity.
     */
    public Entity newCamera(String name, boolean main)
    {
        Entity entity = newEntity(name, 0);
        Camera camera = new Camera(0, context.getDisplay().getWidth(),0, context.getDisplay().getHeight());

        entity.addComponent(camera);

        if (main)
            camera.setAsMain();

        return entity;
    }

    /**
     * Constructs a new {@link Entity} with a sprite component and add it to the hierarchy.
     *
     * @return the new {@link Entity} with a sprite.
     */
    public final Entity newEntity(String name, int layer, Texture texture)
    {
        Entity entity = newEntity(name, layer);
        entity.addComponent(new Sprite(texture));
        return entity;
    }

    public void dispose()
    {
        for (Entity entity : entities)
            entity.dispose();
    }

    public void setCreated(boolean created)
    {
        this.created = created;
    }

    public boolean isCreated()
    {
        return created;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }

    public int getEntitiesCount()
    {
        return entities.size();
    }
}
