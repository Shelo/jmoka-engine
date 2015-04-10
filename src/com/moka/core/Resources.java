package com.moka.core;

import com.moka.core.xml.XmlResourcesReader;
import com.moka.utils.JMokaException;
import com.moka.graphics.Texture;

import java.util.HashMap;

public class Resources extends SubEngine
{
    private XmlResourcesReader resourcesReader = new XmlResourcesReader(this);
    private HashMap<String, Object> resources = new HashMap<>();
    private HashMap<String, Texture> textures = new HashMap<>();

    /**
     * Loads resources and add them to this resources manager. Note that the XML file
     * should follow the structure given in the resources scheme.
     *
     * @param filePath the path to the xml.
     */
    public void loadResources(String filePath)
    {
        resourcesReader.read(filePath);
    }

    /**
     * Returns the object with the given name. Throw an exception if nothing was found.
     *
     * @param name  the name of the resource.
     * @return the resource as an object.
     */
    public Object get(String name)
    {
        if (!resources.containsKey(name))
        {
            throw new JMokaException("Resource with name " + name + " doesn't exists.");
        }

        return resources.get(name);
    }

    /**
     * Returns the resource as an string. This calls get(name) and cast the result.
     * An exception is thrown if nothing was found.
     *
     * @param name  the name of the resource.
     * @return the string resource.
     */
    public String getString(String name)
    {
        return (String) resources.get(name);
    }

    /**
     * Returns the resource as an int. This calls get(name) and cast the result.
     * An exception is thrown if nothing was found.
     *
     * @param name  the name of the resource.
     * @return the int resource.
     */
    public int getInt(String name)
    {
        return (int) get(name);
    }

    /**
     * Returns the resource as an int. This calls get(name) and cast the result.
     * If nothing was found, return the default value.
     *
     * @param name  the name of the resource.
     * @return the int resource.
     */
    public int getIntOr(String name, int defaultValue)
    {
        if (has(name))
        {
            return (int) get(name);
        }
        else
        {
            return defaultValue;
        }
    }

    /**
     * Returns the resource as a float. This calls get(name) and cast the result.
     * An exception is thrown if nothing was found.
     *
     * @param name  the name of the resource.
     * @return the float resource.
     */
    public float getFloat(String name)
    {
        return (float) get(name);
    }

    /**
     * Returns the resource as a double. This calls get(name) and cast the result.
     * An exception is thrown if nothing was found.
     *
     * @param name  the name of the resource.
     * @return the double resource.
     */
    public double getDouble(String name)
    {
        return (double) get(name);
    }

    /**
     * Returns the resource as a boolean. This calls get(name) and cast the result.
     * An exception is thrown if nothing was found.
     *
     * @param name  the name of the resource.
     * @return the boolean resource.
     */
    public boolean getBoolean(String name)
    {
        return (boolean) get(name);
    }

    /**
     * Returns true if the resource manager has a certain resource.
     */
    public boolean has(String resource)
    {
        return resources.containsKey(resource);
    }

    /**
     * Adds a resource with a name.
     *
     * @param name  the name of the resource.
     * @param value the value.
     * @return the same value given.
     */
    public Object addResource(String name, Object value)
    {
        resources.put(name, value);
        return value;
    }

    /**
     * Adds a texture to the resource manager. Note that texture are a separated thing to the resource manager.
     * You cannot call: get(textureName), instead you should call getTexture(textureName).
     *
     * @param path      the path to the texture.
     * @param texture   the texture to save.
     * @return the same texture given.
     */
    public Texture addTexture(String path, Texture texture)
    {
        textures.put(path, texture);
        return texture;
    }

    /**
     * Returns a saved texture or throws an exception.
     *
     * @param path the path of the texture.
     * @return the texture.
     */
    public Texture getTexture(String path)
    {
        if (!textures.containsKey(path))
        {
            throw new JMokaException("Texture with path " + path + " doesn't exists.");
        }

        return textures.get(path);
    }

    /**
     * Checks if the resource manager has a texture registered.
     *
     * @return true if it has it.
     */
    public boolean hasTexture(String path)
    {
        return textures.containsKey(path);
    }

}
