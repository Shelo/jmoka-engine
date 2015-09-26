package com.moka.scene;

import com.moka.core.SubEngine;
import com.moka.scene.entity.Entity;
import com.moka.graphics.Shader;
import com.moka.prefabs.OpingPrefabReader;
import com.moka.prefabs.PrefabReader;
import com.moka.utils.JMokaException;

import java.util.ArrayList;
import java.util.List;

public class Context extends SubEngine
{
    private ArrayList<Integer> layers = new ArrayList<>();
    private ArrayList<Entity> entities = new ArrayList<>();
    private PrefabReader prefabReader = new OpingPrefabReader();

    /**
     * Store all scenes.
     */
    private final ArrayList<Scene> scenes = new ArrayList<>();
    private int currentScene;
    private boolean created;

    public final void update()
    {
        for (int i = entities.size() - 1; i >= 0; i--)
        {
            entities.get(i).update();
        }
    }

    public final void create()
    {
        createScene(scenes.get(currentScene));

        for (Entity entity : entities)
        {
            entity.create();
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

    public void addScene(Scene scene)
    {
        scenes.add(scene);
    }

    public void setMainScene(Class<? extends Scene> sceneClass)
    {
        for (int i = 0; i < scenes.size(); i++)
        {
            Scene scene = scenes.get(i);
            if (sceneClass.isInstance(scene))
            {
                log("Loading main scene: " + scene.getClass().getSimpleName());

                // switch to the new scene.
                currentScene = i;

                return;
            }
        }
    }

    public void loadScene(Class<? extends Scene> sceneClass, boolean exitPrevious)
    {
        if (!application.isCreated())
        {
            throw new JMokaException("Can't load scenes until the engine starts.");
        }

        for (int i = 0; i < scenes.size(); i++)
        {
            Scene scene = scenes.get(i);
            if (sceneClass.isInstance(scene))
            {
                log("Loading scene: " + scene.getClass().getSimpleName());

                // unload the previous scene.
                Scene previousScene = scenes.get(currentScene);
                exitScene(previousScene, exitPrevious);
                hardReset();

                // switch to the new scene.
                currentScene = i;
                create();

                return;
            }
        }

        // the scene could not be found. Error!.
        throw new JMokaException("Scene does not exists in the context.");
    }

    public Scene getScene(Class<? extends Scene> sceneClass)
    {
        for (Scene scene : scenes)
        {
            if (sceneClass.isInstance(scene))
            {
                return scene;
            }
        }

        // the scene could not be found. Error!.
        throw new JMokaException("Scene does not exists in the context.");
    }

    private void exitScene(Scene scene, boolean exit)
    {
        if (exit)
        {
            scene.onExit();
            scene.setCreated(false);
        }
        else
        {
            scene.onLeave();
        }
    }

    private void createScene(Scene scene)
    {
        if (scene.isCreated())
        {
            scene.onResume();
        }
        else
        {
            created = false;
            scene.setContext(this);
            scene.onCreate();
            created = true;
        }
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

    public void dispose()
    {
        for (Entity entity : entities)
        {
            entity.dispose();
        }
    }

    public PrefabReader getPrefabReader()
    {
        return prefabReader;
    }

    public boolean isCreated()
    {
        return created;
    }

    public int getEntitiesCount()
    {
        return entities.size();
    }
}
