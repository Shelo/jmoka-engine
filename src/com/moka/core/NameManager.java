package com.moka.core;

import com.moka.core.entity.Component;
import com.moka.utils.JMokaException;

import java.util.HashMap;

public class NameManager extends SubEngine
{
    public static final String DEFAULT_NAMESPACE = "Moka";

    private HashMap<String, Package> packages = new HashMap<>();

    public NameManager()
    {
        usePackage(DEFAULT_NAMESPACE, "com.moka.components");
    }

    /**
     * Tells the engine that you will be using a package by the name of <i>name</i>.
     *
     * @param name          handy name for the package.
     * @param container     path to the Java package.
     */
    public void usePackage(String name, String container)
    {
        try
        {
            Class<?> manifest = Class.forName(container + ".PackageManifest");
            Package instance = (Package) manifest.newInstance();
            instance.register();
            packages.put(name, instance);
        }
        catch (ClassNotFoundException e)
        {
            throw new JMokaException("Package does not exists or does not have a manifest.");
        }
        catch (InstantiationException e)
        {
            throw new JMokaException("The manifest of the package cannot be instantiated.");
        }
        catch (IllegalAccessException e)
        {
            throw new JMokaException("Error while accessing the package manifest.");
        }
    }

    public Class<? extends Component> findClass(String namespace, String name)
    {
        if (!packages.containsKey(namespace))
        {
            throw new JMokaException("The package " + namespace + " does not exists.");
        }

        Package manifest = packages.get(namespace);

        Class<? extends Component> component = manifest.getComponent(name);

        if (component == null)
        {
            throw new JMokaException("The package " + namespace + " does not contains the component " + name);
        }

        return component;
    }
}
