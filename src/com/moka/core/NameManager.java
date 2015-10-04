package com.moka.core;

import com.moka.scene.entity.Component;
import com.moka.utils.CoreUtil;
import com.moka.utils.JMokaException;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class NameManager extends SubEngine
{
    public static final String DEFAULT_NAMESPACE = "Moka";
    private static final String MANIFEST_TEMPLATE =
            "package %s;\n" +
            "\n" +
            "import com.moka.core.Package;\n" +
            "import com.moka.scene.entity.Component;\n" +
            "\n" +
            "import java.util.LinkedList;\n" +
            "\n" +
            "public class PackageManifest extends Package\n" +
            "{\n" +
            "    @Override\n" +
            "    public void registerComponents(LinkedList<Class<? extends Component>> components)\n" +
            "    {\n" +
            "%s\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public String getCommonName()\n" +
            "    {\n" +
            "        return \"%s\";\n" +
            "    }\n" +
            "}\n";;

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
     * Tells the engine that you will be using a package by the name of <i>name</i>.
     *
     * @param location  the location of the package.
     */
    public void usePackage(String location)
    {
        usePackage(null, location);
    }

    /**
     * Use a package with the common package name.
     *
     * ** NOTE: ONLY USE IN DEVELOPMENT.
     *
     * @param name              name for the package.
     * @param dir               src directory of the project.
     * @param location          the location of the package (ex: com.moka...).
     * @param exportManifest    should this export a manifest for this package.
     */
    @SuppressWarnings("unchecked")
    public void usePackage(String name, String dir, String location, boolean exportManifest)
    {
        // construct full path.
        StringBuilder builder = new StringBuilder();
        builder.append(dir);

        // remove last separator if the dir contains one.
        if (builder.charAt(builder.length() - 1) == File.separator.charAt(0))
            builder.deleteCharAt(builder.length() - 1);

        // construct the full path with the dir and location.
        String[] parts = location.split("\\.");
        for (String part : parts)
            builder.append(File.separator).append(part);
        String path = builder.toString();

        // open the directory and check that exists.
        File directory = new File(path);
        if (!directory.exists())
            throw new JMokaException("Directory " + path + " for package " + name + " does not exists.");

        String[] classes = directory.list();
        LinkedList<Class<? extends Component>> components = new LinkedList<>();

        for (String className : classes)
        {
            if (className.endsWith(".java"))
            {
                try
                {
                    Class<?> component = Class.forName(location + "." + className.substring(0,
                            className.length() - 5));

                    if (Component.class.isAssignableFrom(component))
                        components.add((Class<? extends Component>) component);
                }
                catch (ClassNotFoundException e)
                {
                    throw new JMokaException("Undefined error while running automatic package explorer.");
                }
            }
        }

        Package manifest = new Package()
        {
            @Override
            public void registerComponents(LinkedList<Class<? extends Component>> c)
            {
                c.addAll(components);
            }

            @Override
            public String getCommonName()
            {
                return name;
            }
        };

        // if the exportManifest boolean is set to true then construct the package manifest file and save
        // it to the location specified.
        if (exportManifest)
        {
            StringBuilder listOfComponents = new StringBuilder();
            for (Class<? extends Component> component : components)
                listOfComponents.append("        components.add(")
                        .append(component.getSimpleName()).append(".class);\n");

            CoreUtil.overrideFileWith(path + File.separator + "PackageManifest.java",
                    String.format(MANIFEST_TEMPLATE, location, listOfComponents.toString(), name));
        }

        usePackage(manifest);
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
