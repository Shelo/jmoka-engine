package com.moka.core;

import com.moka.scene.entity.Component;

import java.util.LinkedList;

public abstract class Package
{
    private LinkedList<Class<? extends Component>> components;

    public final void register()
    {
        components = new LinkedList<>();
        registerComponents(components);
    }

    public final boolean hasComponent(String name)
    {
        for (Class<? extends Component> component : components)
        {
            if (component.getSimpleName().equals(name))
                return true;
        }

        return false;
    }

    public final boolean hasComponent(Class<? extends Component> componentClass)
    {
        return components.contains(componentClass);
    }

    public Class<? extends Component> getComponent(String name)
    {
        for (Class<? extends Component> component : components)
        {
            if (component.getSimpleName().equals(name))
                return component;
        }

        return null;
    }

    public LinkedList<Class<? extends Component>> getComponents()
    {
        return components;
    }

    public abstract String getCommonName();
    public abstract void registerComponents(LinkedList<Class<? extends Component>> components);
}
