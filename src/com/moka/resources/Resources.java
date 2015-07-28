package com.moka.resources;

import com.moka.core.Moka;
import com.moka.prefabs.Prefab;
import com.moka.graphics.Texture;
import com.moka.utils.JMokaException;

import java.lang.reflect.Field;

public abstract class Resources
{
    // Define all built-in resource loaders.
    public static class TextureLoader extends ResourceLoader
    {
        @Override
        public Object load(String path)
        {
            return new Texture(path);
        }
    }

    public static class PrefabLoader extends ResourceLoader
    {
        @Override
        public Object load(String path)
        {
            return Moka.getContext().getPrefabReader().newPrefab(path);
        }
    }

    private String root;

    // Loaders.
    private TextureLoader textureLoader = new TextureLoader();
    private PrefabLoader prefabLoader = new PrefabLoader();

    public Resources(String root)
    {
        this.root = root;
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

    public Object findResource(String reference)
    {
        String[] parts = reference.split("\\.");

        Class<?> container = this.getClass();
        for (int i = 0; i < parts.length - 1; i++)
        {
            Class[] classes = container.getDeclaredClasses();

            for (Class innerClass : classes)
            {
                if (innerClass.getSimpleName().equals(parts[i]))
                {
                    container = innerClass;
                    break;
                }
            }
        }

        try
        {
            // retrieve the value (the last of the reference) inside the container.
            Field field = container.getField(parts[parts.length - 1]);
            return field.get(null);
        }
        catch (NoSuchFieldException e)
        {
            throw new JMokaException("Resource '" + reference + "' does not exists.");
        }
        catch (IllegalAccessException e)
        {
            throw new JMokaException("Resource '" + reference + "' is not accessible.");
        }
    }

    public void internalLoad()
    {
        inspectBindLoads();
        load();
    }

    /**
     * This is the place to load all things. Remember that the order does matters, if a prefab needs some
     * resource that has not been initialized at the time, the resources will be null.
     */
    public void load()
    {

    }

    /**
     * Dispose everything you want.
     */
    public void dispose()
    {

    }

    private void inspectBindLoads()
    {
        for (Class<?> innerClass : getClass().getDeclaredClasses())
        {
            BindLoad bindLoad = innerClass.getAnnotation(BindLoad.class);

            if (bindLoad != null)
            {
                bindLoad(root, innerClass, bindLoad, false);
            }
        }
    }

    /**
     * Loads an inner resource class. This is specially created to load bindings that where skipped
     * due to dependencies (an example is when loading prefabs, which depends on textures and sounds to be loaded
     * first).
     *
     * A note: The class has to be an inner class.
     *
     * @param res   the inner class.
     */
    public void bindLoad(Class<?> res)
    {
        BindLoad bindLoad = res.getAnnotation(BindLoad.class);

        if (bindLoad != null)
        {
            bindLoad(root, res, bindLoad, true);
        }
    }

    private void bindLoad(String root, Class<?> res, BindLoad bind, boolean noSkip)
    {
        if (!noSkip && bind.skip())
        {
            return;
        }

        ResourceLoader loader = null;

        switch (bind.loader())
        {
            case TEXTURE:
                loader = textureLoader;
                break;
            case PREFAB:
                loader = prefabLoader;
                break;
        }

        for (Field field : res.getDeclaredFields())
        {
            bindResource(root, field, loader, bind);
        }

        // Inspect sub classes.
        for (Class<?> innerClass : res.getDeclaredClasses())
        {
            BindLoad subBind = innerClass.getAnnotation(BindLoad.class);
            if (subBind != null)
            {
                bindLoad(root + bind.path(), innerClass, subBind, false);
            }
        }
    }

    private String getExtension(BindLoad bind, BindConfig config)
    {
        if (config == null)
        {
            return bind.extension();
        }

        return config.extension().isEmpty() ? bind.extension() : config.extension();
    }

    private void bindResource(String root, Field field, ResourceLoader loader, BindLoad bind)
    {
        BindConfig config = field.getAnnotation(BindConfig.class);

        // do nothing if the user wants to skip this.
        if (config != null && config.skip())
        {
            return;
        }

        String extension = getExtension(bind, config);
        String path = getPath(bind, config);
        String file = buildPath(root + path, field.getName(), extension);

        try
        {
            field.set(this, loader.load(file));
        }
        catch (IllegalAccessException e)
        {
            throw new JMokaException("Error while trying to load " + field.getName()
                    + ", maybe the field is not accessible?");
        }
    }

    private String getPath(BindLoad bind, BindConfig config)
    {
        if (config == null)
        {
            return bind.path();
        }

        return config.path().isEmpty() ? bind.path() : config.path();
    }

    private String buildPath(String dir, String baseName, String ext)
    {
        return dir + baseName + "." + ext;
    }
}
