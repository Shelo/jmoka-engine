package com.moka.core;

import com.moka.components.Component;
import com.moka.components.Sprite;
import com.moka.core.subengines.Context;
import com.moka.exceptions.JMokaException;
import com.moka.math.Matrix4;
import com.moka.math.Vector2f;
import com.moka.physics.Collider;
import com.moka.physics.Collision;

import java.util.ArrayList;

public class Entity
{
    private final ArrayList<Component> components;
    private final Transform transform;
    private final String name;

    private boolean destroyed;
    private Collider collider;
    private Context context;
    private Sprite sprite;

    public Entity(String name, Context context)
    {
        this.context = context;
        this.name = name;

        components = new ArrayList<>();
        transform = new Transform(this);
    }

    public Entity addComponent(Component component)
    {
        component.setEntity(this);

        if (component instanceof Sprite)
        {
            sprite = (Sprite) component;
        }
        else if (component instanceof Collider)
        {
            collider = (Collider) component;
        }
        else
        {
            components.add(component);
        }

        return this;
    }

    public void create()
    {
        if (hasSprite())
        {
            sprite.onCreate();
        }

        if (hasCollider())
        {
            collider.onCreate();
        }

        for (Component component : components)
        {
            if (component.isEnabled())
            {
                component.onCreate();
            }
        }
    }

    public void update()
    {
        transform.update();
        for (Component component : components)
        {
            if (component.isEnabled())
            {
                component.onUpdate();
            }
        }
    }

    public void collide(Collision collision)
    {
        for (Component component : components)
        {
            if (component.isEnabled())
            {
                component.onCollide(collision);
            }
        }
    }

    public void postUpdate()
    {
        for (Component component : components)
        {
            if (component.isEnabled())
            {
                component.onPostUpdate();
            }
        }
    }

    public void destroy()
    {
        destroyed = true;

        for (Component component : components)
        {
            if (component.isEnabled())
            {
                component.onDestroy();
            }
        }
    }

    public <T> T getComponent(Class<T> componentClass)
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

    public Collider getCollider()
    {
        return collider;
    }

    public Sprite getSprite()
    {
        return sprite;
    }

    public String getName()
    {
        return name;
    }

    public Vector2f[] transformVertices(final Vector2f[] vertices)
    {
        Vector2f[] res = new Vector2f[vertices.length];
        Matrix4 model = CoreUtils.getModelMatrix(getTransform());

        for (int i = 0; i < vertices.length; i++)
        {
            res[i] = model.mul(vertices[i]);
        }

        return res;
    }

    public boolean hasSprite()
    {
        return sprite != null;
    }

    public boolean hasCollider()
    {
        return collider != null;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public Context getContext()
    {
        return context;
    }
}
