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
            e.printStackTrace();
            throw new JMokaException("Resource name inside " + innerClassName + " does not exists.");
        }
        catch (IllegalAccessException e)
        {
            throw new JMokaException("Resource " + innerClassName + "." + name + " is not accessible.");
        }
    }

    public abstract void load();
    public abstract void dispose();
}
