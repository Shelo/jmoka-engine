package com.moka.core;

import com.moka.components.Sprite;
import com.moka.math.Matrix3;
import com.moka.math.Vector2;
import com.moka.physics.Collider;
import com.moka.physics.Collision;
import com.moka.utils.CalcUtil;

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

        if (hasCollider())
        {
            getCollider().onUpdate();
        }

        if (hasSprite())
        {
            getSprite().onUpdate();
        }

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
        if (hasCollider())
        {
            getCollider().onCollide(collision);
        }

        if (hasSprite())
        {
            getSprite().onCollide(collision);
        }

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
        if (hasCollider())
        {
            getCollider().onPostUpdate();
        }

        if (hasSprite())
        {
            getSprite().onPostUpdate();
        }

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

    public Vector2[] transformVertices(final Vector2[] vertices)
    {
        Vector2[] res = new Vector2[vertices.length];
        Matrix3 model = CalcUtil.calcModelMatrix(getTransform());

        for (int i = 0; i < vertices.length; i++)
        {
            res[i] = new Vector2();
            model.mul(vertices[i], res[i]);
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
