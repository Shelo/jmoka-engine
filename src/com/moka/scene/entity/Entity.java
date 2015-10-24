package com.moka.scene.entity;

import com.moka.graphics.Drawable;
import com.moka.scene.Scene;

import java.util.ArrayList;

public class Entity
{
    private final ArrayList<Component> components;
    private final Transform transform;
    private final String name;

    private boolean destroyed;
    private Drawable drawable;
    private String group;
    private Scene scene;

    public Entity(String name)
    {
        this.name = name;

        components = new ArrayList<>();
        transform = new Transform(this);
    }

    public Entity(String name, Scene scene)
    {
        this.scene = scene;
        this.name = name;

        components = new ArrayList<>();
        transform = new Transform(this);
    }

    public Entity addComponent(Component component)
    {
        component.setEntity(this);

        if (component instanceof Drawable)
            drawable = (Drawable) component;
        else
            components.add(component);

        return this;
    }

    public void create()
    {
        if (hasDrawable())
            drawable.onCreate();

        for (Component component : components)
        {
            if (component.isEnabled())
                component.onCreate();
        }
    }

    public void update()
    {
        transform.update();

        if (hasDrawable())
            drawable.onUpdate();

        for (Component component : components)
        {
            if (component.isEnabled())
                component.onUpdate();
        }
    }

    public void postUpdate()
    {
        if (hasDrawable())
            drawable.onPostUpdate();

        for (Component component : components)
        {
            if (component.isEnabled())
                component.onPostUpdate();
        }
    }

    public void destroy()
    {
        if (destroyed)
            return;

        destroyed = true;
    }

    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        for (Component component : components)
        {
            if (componentClass.isInstance(component))
            {
                return componentClass.cast(component);
            }
        }

        return null;
    }

    public Transform getTransform()
    {
        return transform;
    }

    public Drawable getDrawable()
    {
        return drawable;
    }

    public String getName()
    {
        return name;
    }

    public void dispose()
    {
        getTransform().dispose();

        for (Component component : components)
        {
            component.onDispose();
        }
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public String getGroup()
    {
        return group;
    }

    public boolean belongsTo(String groupName)
    {
        return groupName.equals(group);
    }

    public boolean hasDrawable()
    {
        return drawable != null;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public Scene getScene()
    {
        return scene;
    }

    public void onDestroy()
    {
        for (Component component : components)
        {
            if (component.isEnabled())
            {
                component.onDestroy();
            }
        }
    }

    public boolean isNamed(String name)
    {
        return this.name.equals(name);
    }

    @Override
    public String toString()
    {
        return name;
    }
}
