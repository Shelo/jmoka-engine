package com.moka.resources;

import com.moka.core.Moka;
import com.moka.graphics.Texture;
import com.moka.prefabs.Prefab;
import com.moka.utils.ConfigDataFile;
import com.moka.utils.FileHandle;
import com.moka.utils.JMokaException;
import com.moka.utils.JMokaLog;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Extend this class to use resource references into Prefab files, also this gives
 * a nice way for loading resources easily.
 * <p>
 * Usage:
 * <pre>
 * {@code
 * public class R extends Resources
 * {
 *      public static class axes
 *      {
 *          public static final String HORIZONTAL = "horizontal";
 *          public static final String VERTICAL = "vertical";
 *      }
 *
 *      public static class buttons
 *      {
 *          public static final String FIRE_1 = "fire1";
 *      }
 *
 *      public static class screen
 *      {
 *          public static int WIDTH = 64 * 13;
 *          public static int HEIGHT = 64 * 8;
 *      }
 *
 *      \@BindLoad(path = "img/", extension = "png")
 *      public static class textures
 *      {
 *          public static Texture hotline;
 *          public static Texture enemy02;
 *
 *          \@BindLoad(path = "tiles/", extension = "png")
 *          public static class tiles
 *          {
 *              public static Texture dirt01;
 *          }
 *      }
 *
 *      \@BindLoad(path = "data/", extension = "oping")
 *      public static class data
 *      {
 *          public static ConfigDataFile enemies;
 *      }
 *
 *      \@BindLoad(delay = true, path = "prefabs/integration/", extension = "oping")
 *      public static class prefabs
 *      {
 *          public static Prefab hotline;
 *          public static Prefab enemy;
 *          public static Prefab tileMap;
 *
 *          \@BindLoad(path = "tiles/", extension = "oping")
 *          public static class tiles
 *          {
 *              public static Prefab dirt01;
 *          }
 *      }
 *
 *      public R(String root)
 *      {
 *          super(root);
 *      }
 * }
 * }
 * </pre>
 */
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

    public static class FileHandleLoader extends ResourceLoader
    {
        @Override
        public Object load(String path)
        {
            return new FileHandle(path);
        }
    }

    public static class ConfigDataFileLoader extends ResourceLoader
    {
        @Override
        public Object load(String path)
        {
            return new ConfigDataFile(path);
        }
    }

    private String root;

    // Loaders.
    // TODO: support custom loaders.
    private TextureLoader textureLoader = new TextureLoader();
    private PrefabLoader prefabLoader = new PrefabLoader();
    private ResourceLoader fileHandleLoader = new FileHandleLoader();
    private ConfigDataFileLoader configDataFileLoader = new ConfigDataFileLoader();

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
        for (int i = 0; i < parts.length - 1; i++) {
            Class[] classes = container.getDeclaredClasses();

            boolean found = false;
            for (Class innerClass : classes) {
                if (innerClass.getSimpleName().equals(parts[i])) {
                    found = true;
                    container = innerClass;
                    break;
                }
            }

            if (!found) {
                throw new JMokaException("Cannot find resource: " + reference);
            }
        }

        try {
            // retrieve the value (the last of the reference) inside the container.
            Field field = container.getField(parts[parts.length - 1]);
            return field.get(null);
        } catch (NoSuchFieldException e) {
            throw new JMokaException("Resource '" + reference + "' does not exists.");
        } catch (IllegalAccessException e) {
            throw new JMokaException("Resource '" + reference + "' is not accessible.");
        }
    }

    public void internalLoad()
    {
        inspectBindLoads();
        load();
        JMokaLog.o("Resources", "All loaded.");
    }

    /**
     * Override if some resources needs a special way of loading.
     */
    public void load()
    {

    }

    /**
     * Override to dispose loaded resources if needed.
     */
    public void dispose()
    {

    }

    private void inspectBindLoads()
    {
        bindLoad(root, getClass(), null, false);

        // TODO: why is this commented-out ?
        /*
        for (Class<?> innerClass : getClass().getDeclaredClasses()) {
            BindLoad bindLoad = innerClass.getAnnotation(BindLoad.class);

            if (bindLoad != null) {
                bindLoad(root, innerClass, bindLoad, false);
            }
        }
        */
    }

    /**
     * Loads an inner resource class. This is specially created to load bindings that where skipped
     * due to dependencies (an example is when loading prefabs, which depends on textures and sounds
     * to be loaded first).
     * <p>
     * A note: The class has to be an inner class.
     * <p>
     * <p>
     * See {@link BindLoad#delay()} for a more automated process.
     *
     * @param res the inner class.
     */
    public void bindLoad(Class<?> res)
    {
        BindLoad bindLoad = res.getAnnotation(BindLoad.class);

        if (bindLoad != null) {
            bindLoad(root, res, bindLoad, true);
        }
    }

    private void bindLoad(String root, Class<?> res, BindLoad bind, boolean noSkip)
    {
        // the bind annotation can be null in case of the root object, even more,
        // since this is a root object, it can't load direct fields neither.
        if (bind != null) {
            if (!noSkip && bind.skip()) {
                return;
            }

            for (Field field : res.getDeclaredFields()) {
                bindResource(root, field, bind);
            }
        }

        ArrayList<Class<?>> delayed = new ArrayList<>();

        // Inspect sub classes.
        for (Class<?> innerClass : res.getDeclaredClasses()) {
            BindLoad subBind = innerClass.getAnnotation(BindLoad.class);

            // skip if this doesn't has the BindLoad annotation.
            if (subBind != null) {
                if (subBind.delay()) {
                    delayed.add(innerClass);
                } else {
                    String path = root + (bind == null ? "" : bind.path());
                    bindLoad(path, innerClass, subBind, false);
                }
            }
        }

        // load delayed sub classes.
        for (Class<?> delayedClass : delayed) {
            BindLoad subBind = delayedClass.getAnnotation(BindLoad.class);
            String path = root + (bind == null ? "" : bind.path());
            bindLoad(path, delayedClass, subBind, false);
        }
    }

    private String getExtension(BindLoad bind, BindConfig config)
    {
        if (config == null) {
            return bind.extension();
        }

        return config.extension().isEmpty() ? bind.extension() : config.extension();
    }

    private void bindResource(String root, Field field, BindLoad bind)
    {
        BindConfig config = field.getAnnotation(BindConfig.class);

        // do nothing if the user wants to skip this.
        if (config != null && config.skip()) {
            return;
        }

        String extension = getExtension(bind, config);
        String path = getPath(bind, config);
        String file = buildPath(root + path, field.getName(), extension);

        try {
            ResourceLoader loader = getLoader(field.getType());

            if (loader != null) {
                field.set(this, loader.load(file));
            } else {
                throw new JMokaException("Not supported loader for type: " + field.getType());
            }
        } catch (IllegalAccessException e) {
            throw new JMokaException("Error while trying to load " + field.getName()
                    + ", maybe the field is not accessible?");
        }
    }

    private ResourceLoader getLoader(Class<?> type)
    {
        if (type == Texture.class) {
            return textureLoader;
        } else if (type == Prefab.class) {
            return prefabLoader;
        } else if (type == FileHandle.class) {
            return fileHandleLoader;
        } else if (type == ConfigDataFile.class) {
            return configDataFileLoader;
        }

        return null;
    }

    private String getPath(BindLoad bind, BindConfig config)
    {
        if (config == null) {
            return bind.path();
        }

        return config.path().isEmpty() ? bind.path() : config.path();
    }

    private String buildPath(String dir, String baseName, String ext)
    {
        return dir + baseName + "." + ext;
    }
}
