package com.moka.core;

import com.moka.core.entity.Component;
import com.moka.core.entity.Entity;
import com.moka.graphics.Shader;
import com.moka.prefabs.OpingPrefabReader;
import com.moka.prefabs.PrefabReader;
import com.moka.utils.JMokaException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Context extends SubEngine
{
    private HashMap<String, Entity> nameRelations;
    private ArrayList<ArrayList<Entity>> layers;
    private PrefabReader prefabReader;

    /**
     * Used to store all layers.
     */
    private final ArrayList<Entity> groupEntities = new ArrayList<>();

    /**
     * Store all scenes.
     */
    private final ArrayList<Scene> scenes = new ArrayList<>();
    private int currentScene;
    private boolean created;

    public Context()
    {
        prefabReader = new OpingPrefabReader();
        nameRelations = new HashMap<>();
        layers = new ArrayList<>();
    }

    public final void update()
    {
        for (int l = layers.size() - 1; l >= 0; l--)
        {
            ArrayList<Entity> layer = layers.get(l);

            for (int i = layer.size() - 1; i >= 0; i--)
            {
                /*
                Runnable runnable = Threading.runnable(ActionDelegator.update, layer.get(i));
                service.execute(runnable);
                */
                layer.get(i).update();
            }
        }
    }

    public final void create()
    {
        createScene(scenes.get(currentScene));

        for (ArrayList<Entity> layer : layers)
        {
            // iterate over entities.
            for (int i = layer.size() - 1; i >= 0; i--)
            {
                layer.get(i).create();
            }
        }
    }

    public final void render(Shader shader)
    {
        for (ArrayList<Entity> layer : layers)
        {
            for (Entity entity : layer)
            {
                if (entity.hasSprite() && entity.getSprite().isEnabled())
                {
                    entity.getSprite().render(shader);
                }
            }
        }
    }

    public void postUpdate()
    {
        for (ArrayList<Entity> layer : layers)
        {
            for (Entity entity : layer)
            {
                entity.postUpdate();
            }
        }
    }

    public void clean()
    {
        for (ArrayList<Entity> layer : layers)
        {
            for (int i = layer.size() - 1; i >= 0; i--)
            {
                Entity entity = layer.get(i);

                if (entity.isDestroyed())
                {
                    entity.onDestroy();
                    layer.remove(i);
                    nameRelations.remove(entity.getName());
                }
            }
        }
    }

    public void hardReset()
    {
        layers.clear();
        nameRelations.clear();
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
        // if the layer doesn't exists...
        while (layers.size() <= layer)
        {
            layers.add(new ArrayList<>());
        }

        layers.get(layer).add(entity);
        return entity;
    }

    /**
     * Constructs a new {@link Entity} and add it to the hierarchy.
     *
     * @return the new {@link Entity}.
     */
    public final Entity newEntity(String name, int layer)
    {
        // throw exception if the name already exists.
        if (nameRelations.containsKey(name))
        {
            throw new JMokaException("Entity with name " + name + " already exists.");
        }

        // create and add the getEntity to the game.
        Entity entity = new Entity(name, this);
        addEntity(entity, layer);

        // register the name only if it has a name.
        if (name != null)
            nameRelations.put(name, entity);

        return entity;
    }

    /**
     * Finds an Entity on the game. Raises an exception if it is not found.
     *
     * @param name getEntity's unique tag.
     * @return the getEntity if found.
     */
    public final Entity findEntity(String name)
    {
        Entity entity = nameRelations.get(name);

        if (entity == null)
        {
            throw new JMokaException("There's no entity with name " + name + ".");
        }

        return entity;
    }

    /**
     * Gets all entities from a group.
     *
     * @return all entities from that group as an array list or null if the list is empty.
     */
    public List<Entity> getEntitiesFromGroup(String group)
    {
        groupEntities.clear();

        for (ArrayList<Entity> layer : layers)
        {
            for (Entity entity : layer)
            {
                if (entity.belongsTo(group))
                {
                    groupEntities.add(entity);
                }
            }
        }

        return groupEntities.isEmpty()? null : groupEntities;
    }

    public void dispose()
    {
        for (ArrayList<Entity> layer : layers)
        {
            for (Entity entity : layer)
            {
                entity.dispose();
            }
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
        return nameRelations.size();
    }
}
