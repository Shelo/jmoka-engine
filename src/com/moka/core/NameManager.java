package com.moka.core;

import com.moka.scene.entity.Component;
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
     * @param name         handy name for the package. If null, use the common package name.
     * @param location     path to the Java package.
     */
    public void usePackage(String name, String location)
    {
        try
        {
            Class<?> manifest = Class.forName(location + ".PackageManifest");
            Package instance = (Package) manifest.newInstance();
            instance.register();
            packages.put(name == null ? instance.getCommonName() : name, instance);
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

    /**
     * Tells the engine that you will be using a package.
     *
     * @param manifest     manifest of the package.
     */
    public void usePackage(Package manifest)
    {
        usePackage(manifest.getCommonName(), manifest);
    }

    /**
     * Tells the engine that you will be using a package by the name of <i>name</i>.
     *
     * @param name         handy name for the package. If null, use the common package name.
     * @param manifest     manifest of the package.
     */
    public void usePackage(String name, Package manifest)
    {
        manifest.register();
        packages.put(name == null ? manifest.getCommonName() : name, manifest);
    }

    /**
     * Use a package with the common package name.
     *
     * @param location  the location of the package.
     */
    public void usePackage(String location)
    {
        usePackage(null, location);
    }

    /**
     * Finds a component class inside the given package with the given name.
     *
     * @param packageName   the package name.
     * @param name          the name of the component.
     * @return              the component class.
     */
    public Class<? extends Component> findComponent(String packageName, String name)
    {
        if (!packages.containsKey(packageName))
            throw new JMokaException("The package " + packageName + " does not exists.");

        Package manifest = packages.get(packageName);

        Class<? extends Component> component = manifest.getComponent(name);

        if (component == null)
            throw new JMokaException("The package " + packageName + " does not contains the component " + name);

        return component;
    }

    public HashMap<String, Package> getPackages()
    {
        return packages;
    }
}
