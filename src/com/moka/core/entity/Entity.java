package com.moka.core.entity;

import com.moka.components.Sprite;
import com.moka.core.Context;
import com.moka.core.Transform;
import com.moka.math.Matrix3;
import com.moka.math.Vector2;
import com.moka.physics.PhysicsBody;
import com.moka.utils.CalcUtil;
import com.moka.utils.Pools;

import java.util.ArrayList;

public class Entity
{
    private final ArrayList<Component> components;
    private final Transform transform;
    private final String name;

    private boolean destroyed;
    private Context context;
    private Sprite sprite;
    private String group;

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

    public void postUpdate()
    {
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
        if (destroyed)
        {
            return;
        }

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

    public Sprite getSprite()
    {
        return sprite;
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

    public Vector2[] transformVertices(final Vector2[] vertices)
    {
        Vector2[] res = new Vector2[vertices.length];
        Matrix3 model = CalcUtil.calcModelMatrix(getTransform());

        for (int i = 0; i < vertices.length; i++)
        {
            res[i] = Pools.vec2.take();
            model.mul(vertices[i], res[i]);
        }

        return res;
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

    public boolean hasSprite()
    {
        return sprite != null;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public Context getContext()
    {
        return context;
    }

    @Override
    public String toString()
    {
        return name;
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
}
