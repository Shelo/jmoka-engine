package com.moka.core;

import com.moka.components.Camera;
import com.moka.core.xml.XmlEntityReader;
import com.moka.core.xml.XmlPrefabReader;
import com.moka.core.xml.XmlSceneReader;
import com.moka.graphics.Shader;
import com.moka.utils.JMokaException;
import com.moka.utils.JMokaLog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base class of a game, takes care of internal core usage.
 */
public abstract class Context extends SubEngine
{
    private static final String TAG = "Context";
    private HashMap<String, Entity> nameRelations;
    private XmlEntityReader entityReader;
    private XmlPrefabReader prefabReader;
    private ArrayList<ArrayList<Entity>> layers;
    private XmlSceneReader sceneReader;
    private String secondaryPackage;

    /**
     * Used to store all layers.
     */
    private final ArrayList<Entity> allEntities = new ArrayList<>();

    public Context()
    {
        nameRelations = new HashMap<>();
        layers = new ArrayList<>();

        // initialize readers.
        sceneReader = new XmlSceneReader(this);
        entityReader = sceneReader.getEntityReader();
        prefabReader = new XmlPrefabReader(entityReader, this);
    }

    public final void update()
    {
        for (int l = layers.size() - 1; l >= 0; l--)
        {
            ArrayList<Entity> layer = layers.get(l);

            for (int i = layer.size() - 1; i >= 0; i--)
            {
                layer.get(i).update();
            }
        }
    }

    public final void create()
    {
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
                if (layer.get(i).isDestroyed())
                {
                    layer.remove(i);
                }
            }
        }
    }

    /**
     * Adds an entity to a given layer.
     *
     * @param entity the entity to be added.
     * @param layer  the layer at which the context will add the entity.
     * @return the same entity given.
     */
    public final Entity addEntity(Entity entity, int layer)
    {
        // if the layer doesn't exists...
        if (layers.size() <= layer)
        {
            // add new layers if needed.
            // TODO: maybe this will change.
            for (int i = 0; i <= layer - layers.size() + 1; i++)
            {
                layers.add(new ArrayList<Entity>());
                JMokaLog.o(TAG, "New layer: " + (i + layers.size() - 1));
            }
        }

        layers.get(layer).add(entity);
        return entity;
    }

    /**
     * Constructs a new {@link com.moka.core.Entity} and add it to the hierarchy.
     * Also says that the entity belongs to this context. That cannot change latter.
     *
     * @return the new {@link com.moka.core.Entity}.
     */
    public final Entity newEntity(String name, int layer)
    {
        // throw exception if the name already exists.
        if (nameRelations.containsKey(name))
        {
            throw new JMokaException("Entity with name " + name + " already exists.");
        }

        // create and add the entity to the game.
        Entity entity = new Entity(name, this);
        addEntity(entity, layer);

        // register the name only if it has a name.
        if (name != null)
        {
            nameRelations.put(name, entity);
        }

        return entity;
    }

    /**
     * Creates a new entity with a camera on it.
     *
     * @param name    name for the entity.
     * @param current sets this camera as the current one.
     * @return the entity.
     */
    public Entity newCamera(String name, boolean current)
    {
        Entity entity = newEntity(name, 0);
        Camera camera = new Camera(0, getDisplay().getWidth(), 0, getDisplay().getHeight());

        entity.addComponent(camera);

        if (current)
        {
            camera.setAsCurrent();
        }

        return entity;
    }

    /**
     * Loads an XML Scene file into the game.
     *
     * @param xmlFilePath xml file path.
     */
    public final void populate(String xmlFilePath)
    {
        sceneReader.read(xmlFilePath);
    }

    /**
     * Defines a secondary package to find components. If the reader fails to find the component
     * in the framework's default package, then it will look in the secondary package. This only
     * works for non package named components in the XML.
     *
     * @param path the absolute path to the package.
     */
    public void setSecondaryPackage(String path)
    {
        this.secondaryPackage = path;
    }

    /**
     * Returns the secondary path in which we could find components classes.
     *
     * @return the path or null if none has been set.
     */
    public String getSecondaryPackage()
    {
        return secondaryPackage;
    }

    /**
     * Creates a new prefab using the XmlPrefabReader defined in this context.
     *
     * @param filePath the path for the XML Entity file.
     * @return the prefab.
     */
    public Prefab newPrefab(String filePath)
    {
        return prefabReader.newPrefab(filePath);
    }

    /**
     * Loads an Entity definition XML file into the game.
     *
     * @param xmlFilePath xml file path.
     * @deprecated use newPrefab to create a prefab object to create new entities.
     */
    public final void readEntity(String xmlFilePath, String name)
    {
        entityReader.read(xmlFilePath, name);
    }

    /**
     * Loads a value definitions file into the game system.
     *
     * @param filePath resource file path.
     */
    public void define(String filePath)
    {
        getResources().loadResources(filePath);
    }

    /**
     * Finds an Entity on the game. Raises an exception if it is not found.
     *
     * @param name entity's unique tag.
     * @return the entity if found.
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
     * Gets all entities across all layers.
     *
     * @return all entities as an array list.
     */
    public ArrayList<Entity> getAllEntities()
    {
        allEntities.clear();

        for (ArrayList<Entity> layer : layers)
        {
            for (Entity entity : layer)
            {
                allEntities.add(entity);
            }
        }

        return allEntities;
    }

    /**
     * Called every update frame, is not necessary for most games do.
     */
    public void onUpdate()
    {

    }

    /**
     * Basically this will help with loading resources before we create the game itself.
     */
    public void onPreLoad()
    {

    }

    /**
     * Creates the game context, this is intended to start everything the game needs.
     */
    public abstract void onCreate();

    /**
     * Been capable to send an error code.
     */
    public abstract void onStop();

}

