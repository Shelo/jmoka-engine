package com.moka.scene;

import com.moka.components.Camera;
import com.moka.components.Sprite;
import com.moka.graphics.Texture;
import com.moka.scene.entity.Entity;
import com.moka.utils.JMokaException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The scene is the element of your game that will actually show something.
 */
public abstract class Scene implements Iterable<Entity>
{
    private static final int LAYERS = 16;

    private ArrayList<List<Entity>> layers = new ArrayList<>(LAYERS);
    private boolean created;
    private Context context;

    public Scene()
    {
        for (int i = 0; i < LAYERS; i++)
            layers.add(new ArrayList<>());
    }

    private class Iter implements Iterator<Entity>
    {
        private Iterator<Entity> iterator;
        private int cursor = 0;

        private Iterator<Entity> nextNonEmpty(boolean update)
        {
            for (int i = cursor + 1; i < layers.size(); i++)
            {
                if (update)
                    cursor = i;

                if (layers.get(i).size() != 0)
                    return layers.get(i).iterator();
            }

            return null;
        }

        @Override
        public boolean hasNext()
        {
            if (cursor == layers.size())
                return false;

            Iterator<Entity> tempIterator = iterator;

            if (tempIterator == null)
                tempIterator = layers.get(cursor).iterator();

            if (!tempIterator.hasNext())
                tempIterator = nextNonEmpty(false);

            return tempIterator != null;
        }

        @Override
        public Entity next()
        {
            if (iterator == null)
                iterator = layers.get(cursor).iterator();

            if (!iterator.hasNext())
                iterator = nextNonEmpty(true);

            return iterator.next();
        }
    }

    /**
     * Called the first time the scene is called to play it self.
     */
    public abstract void onCreate();

    /**
     * Called every frame just before updating every entity.
     * This is intended to be used for managing resources and create custom
     * managers and systems.
     */
    public abstract void onUpdate();

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
        for (int j = layers.size() - 1; j >= 0; j--)
        {
            List<Entity> entities = layers.get(j);

            for (int i = entities.size() - 1; i >= 0; i--)
                entities.get(i).update();
        }
    }

    /**
     * The load method is called whenever the scene needs to show up. This will decide if it
     * needs to be created or resumed.
     */
    public void load(Context context)
    {
        if (created) {
            onResume();
        } else {
            this.context = context;

            onCreate();

            // create every entity.
            for (Entity entity : this)
                entity.create();

            created = true;
        }
    }

    public void postUpdate()
    {
        for (Entity entity : this) {
            entity.postUpdate();
        }
    }

    public void clean()
    {
        for (int j = layers.size() - 1; j >= 0; j--)
        {
            List<Entity> entities = layers.get(j);

            for (int i = entities.size() - 1; i >= 0; i--) {
                if (entities.get(i).isDestroyed()) {
                    entities.get(i).onDestroy();
                    entities.remove(i);
                }
            }
        }
    }

    /**
     * Constructs a new {@link Entity} and add it to the hierarchy.
     *
     * @param name name for the new entity.
     * @param layer layer number for the new entity.
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

        /*for (Entity entity : entities) {
            if (entity.belongsTo(group))
                groupEntities.add(entity);
        }*/

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

        for (Entity entity : this) {
            if (entity.getName() != null && entity.getName().equals(name))
                return entity;
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
        layers.get(layer).add(entity);
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
        Camera camera = new Camera(0, context.getDisplay().getWidth(), 0, context.getDisplay().getHeight());

        entity.addComponent(camera);

        if (main)
            camera.setAsMain();

        return entity;
    }

    /**
     * Constructs a new {@link Entity} with a sprite component and add it to the hierarchy.
     *
     * @return the newly created {@link Entity}.
     */
    public final Entity newEntity(String name, int layer, Texture texture)
    {
        Entity entity = newEntity(name, layer);
        entity.addComponent(new Sprite(texture));
        return entity;
    }

    public void dispose()
    {
        for (List<Entity> layer : layers) {
            for (Entity entity : layer) {
                entity.dispose();
            }
        }
    }

    public void destroy()
    {
        layers.clear();
        // layers.clear();
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
        int count = 0;
        for (List<Entity> layer : layers)
            count += layer.size();

        return count;
    }

    @Override
    public Iterator<Entity> iterator()
    {
        return new Iter();
    }
}
