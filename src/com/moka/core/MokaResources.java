package com.moka.core;

import com.moka.core.Prefab;
import com.moka.graphics.Texture;
import com.moka.utils.JMokaException;

import java.lang.reflect.Field;

public abstract class MokaResources
{
    // TODO: CHANGE THE JMOKA-EXAMPLE THING!!!.
    private Class<?>[] innerClasses;
    private String root;

    public MokaResources(String root)
    {
        this.root = root;

        innerClasses = getClass().getDeclaredClasses();
    }

    public Texture texture(String path)
    {
        return new Texture(root + path);
    }

    public Texture texture(String path, Texture.Filter filter)
    {
        return new Texture(root + path, filter);
    }

    public Prefab prefab(String path)
    {
        return Moka.getContext().getPrefabReader().newPrefab(root + path);
    }

    public void sound(String path)
    {
        // TODO: do something.
    }

    public Class<?> getInnerClass(String name)
    {
        for (Class<?> innerClass : innerClasses)
        {
            if (innerClass.getSimpleName().equals(name))
            {
                return innerClass;
            }
        }

        throw new JMokaException("Resources inner class " + name + " does not exists.");
    }

    public Object findResource(String reference)
    {
        String[] parts = reference.split("\\.");

        if (parts.length != 2)
        {
            throw new JMokaException("The reference must be of the format: innerClass.valueName.");
        }

        return findResource(parts[0], parts[1]);
    }

    public Object findResource(String innerClassName, String name)
    {
        Class<?> innerClass = getInnerClass(innerClassName);

        try
        {
            Field field = innerClass.getField(name);
            return field.get(null);
        }
        catch (NoSuchFieldException e)
        {
            throw new JMokaException("Resource " + name + " inside " + innerClassName + " does not exists.");
        }
        catch (IllegalAccessException e)
        {
            throw new JMokaException("Resource " + innerClassName + "." + name + " is not accessible.");
        }
    }

    /**
     * This is the place to load all things. Remember that the order does matters, if a prefab needs some
     * resource that has not been initialized at the time, the resources will be null.
     */
    public abstract void load();

    /**
     * Dispose everything you want.
     */
    public abstract void dispose();
}
