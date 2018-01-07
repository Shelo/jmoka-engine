package com.moka.scene.entity;

import com.moka.graphics.DrawableComponent;
import com.moka.scene.Scene;
import com.moka.utils.JMokaException;

import java.util.ArrayList;
import java.util.Objects;

public class Entity
{
    private final ArrayList<Component> components;
    private final Transform transform;
    private final String name;

    private boolean destroyed;
    private DrawableComponent drawable;
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

        if (component instanceof DrawableComponent) {
            drawable = (DrawableComponent) component;
        } else {
            components.add(component);
        }

        return this;
    }

    public void create()
    {
        if (hasDrawable()) {
            drawable.onCreate();
        }

        for (Component component : components) {
            if (component.isEnabled()) {
                component.onCreate();
            }
        }
    }

    public void update()
    {
        transform.update();

        if (hasDrawable()) {
            drawable.onUpdate();
        }

        for (Component component : components) {
            if (component.isEnabled()) {
                component.onUpdate();
            }
        }
    }

    public void postUpdate()
    {
        if (hasDrawable()) {
            drawable.onPostUpdate();
        }

        for (Component component : components) {
            if (component.isEnabled()) {
                component.onPostUpdate();
            }
        }
    }

    /**
     * Sets the entity as destroyed, although it will actually be destroyed in the next frame.
     */
    public void destroy()
    {
        if (destroyed) {
            throw new JMokaException("Can't destroy an already destroyed entity, possible" +
                    "programming error.");
        }

        onDestroy();

        destroyed = true;
    }

    /**
     * Finds a component by the given class.
     *
     * Usage:
     * <pre>
     * MyComponent myComponent = myEntity.getComponent(MyComponent.class);
     *
     * if (myComponent != null) {
     *     // component found!
     * }
     * </pre>
     *
     * @param componentClass the component class to be fetched.
     * @return the component object or {@code null} if none.
     */
    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        if (componentClass.isInstance(drawable)) {
            return componentClass.cast(drawable);
        }

        for (Component component : components) {
            if (componentClass.isInstance(component)) {
                return componentClass.cast(component);
            }
        }

        return null;
    }

    /**
     * @return this entity's transform.
     */
    public Transform getTransform()
    {
        return transform;
    }

    /**
     * @return this entity's drawable implementation.
     */
    public DrawableComponent getDrawable()
    {
        return drawable;
    }

    /**
     * @return this entity's defined name (can be null).
     */
    public String getName()
    {
        return name;
    }

    public void dispose()
    {
        getTransform().dispose();

        for (Component component : components) {
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
        for (Component component : components) {
            if (component.isEnabled()) {
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

    /**
     * Checks if the object is an entity with similar characteristics, given by:
     * Entity name, component collection and group.
     *
     * @param obj the object to be tested for equality.
     * @return {@code true} if the objects are equal.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Entity)) {
            return false;
        }

        Entity other = (Entity) obj;

        if (!Objects.equals(other.getName(), getName())) {
            return false;
        }

        if (!Objects.equals(other.getGroup(), getGroup())) {
            return false;
        }

        for (Component component : components) {
            if (!other.components.contains(component)) {
                return false;
            }
        }

        return true;
    }
}
