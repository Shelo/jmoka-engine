package com.moka.core;

import com.moka.core.entity.Component;
import com.moka.core.entity.Entity;
import com.moka.math.Vector2;
import com.moka.triggers.Trigger;
import com.moka.triggers.TriggerPromise;
import com.moka.utils.JMokaException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

/**
 * Creates an prefabricated object, which can instantiate multiple entities using a simple
 * yet easy to use <b>state machine</b>.
 *
 * @author Shelo
 */
public final class Prefab
{
    private PreComponents components = new PreComponents();
    private Vector2 position = new Vector2();
    private Vector2 size = new Vector2();
    private boolean useOwnSize = false;
    private float rotation;
    private String group;
    private int layer;

    /**
     * Creates a new Prefab for a given context with given components.
     * @param components    components.
     */
    public Prefab(PreComponents components)
    {
        this.components = components;
    }

    public PreComponents getComponents()
    {
        return components;
    }

    /**
     * Sets the position for the new instances from now on.
     *
     * @param position position vector.
     */
    public void setPosition(Vector2 position)
    {
        this.position.set(position);
    }

    /**
     * Sets the position for the new instances from now on.
     *
     * @param x the x's position for the vector.
     * @param y the y's position for the vector.
     */
    public void setPosition(float x, float y)
    {
        this.position.set(x, y);
    }

    /**
     * Sets the size from now on, you can always set it to null so the transform uses
     * the size of the sprite.
     *
     * @param size size vector.
     */
    public void setSize(Vector2 size)
    {
        useOwnSize = true;
        this.size.set(size);
    }

    /**
     * Sets the rotation of new entities from now on, in degrees.
     *
     * @param radians angle in radians.
     */
    public void setRotation(float radians)
    {
        this.rotation = radians;
    }

    /**
     * Sets the layer of new entities from now on.
     *
     * @param layer layer integer number.
     */
    public void setLayer(int layer)
    {
        this.layer = layer;
    }

    /**
     * Sets the position of the prefab to the x, y values and the instantiates.
     *
     * @param name  unique name for the new instance.
     * @param x     x position on the world.
     * @param y     y position on the world.
     * @return      the new getEntity.
     */
    public Entity newEntity(String name, float x, float y)
    {
        setPosition(x, y);
        return newEntity(name);
    }

    /**
     * Creates a new instance with using this prefab rules and states. The instance will
     * be automatically added to the stage.
     *
     * @param name  unique name for the new instance.
     * @return      the getEntity.
     */
    public Entity newEntity(String name)
    {
        Entity entity = Moka.getContext().newEntity(name, layer);
        entity.setGroup(group);

        // set transform values for this getEntity.
        Transform transform = entity.getTransform();
        transform.setPosition(position.cpy());
        transform.setRotation(rotation);

        if (useOwnSize)
        {
            transform.setSize(size.cpy());
        }

        // creates every component and adds it to the getEntity.
        for (Class<?> cClass : components.keySet())
        {
            ComponentAttrs componentAttrs = components.get(cClass);
            Component component;

            try
            {
                component = (Component) cClass.newInstance();
                entity.addComponent(component);

                // iterate over the methods and attributes of the component.
                for (Method method : componentAttrs.getKeySet())
                {
                    Object attr = componentAttrs.getValue(method);

                    if (attr == null)
                    {
                        throw new JMokaException("Some error happened... Maybe the resource is null at this time?");
                    }

                    // if the attr is an instance of a trigger, then get a new instance of that trigger
                    // in order to pass it to the setter method.
                    if (attr.getClass().isAssignableFrom(TriggerPromise.class))
                    {
                        attr = Trigger.getNewTriggerInstance(((TriggerPromise) attr).getTriggerClass());
                    }

                    try
                    {
                        method.invoke(component, attr);
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new JMokaException("Cannot set the attribute "
                                + method.getAnnotation(ComponentAttribute.class).value());
                    }
                }

            }
            catch (InstantiationException e)
            {
                throw new JMokaException("Cannot instantiate the component " + cClass.getName()
                        + ", maybe there's no non-args constructor.");
            }
            catch (IllegalAccessException e)
            {
                throw new JMokaException("Cannot access the component " + cClass.getName() + ".");
            }
        }

        // after this we should call the onCreate method on the components.
        if (Moka.getContext().isCreated())
        {
            entity.create();
        }

        return entity;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    /**
     * This is just a hack so I don't have to write that big HashMap every time.
     */
    public static class PreComponents extends HashMap<Class<?>, ComponentAttrs>
    {

    }

    /**
     * Wrapper and helper class to the prefab.
     */
    public static class ComponentAttrs
    {
        private HashMap<Method, Object> methodValues = new HashMap<>();

        public void addMethodValue(Method method, Object value)
        {
            methodValues.put(method, value);
        }

        public Object getValue(Method name)
        {
            return methodValues.get(name);
        }

        public Set<Method> getKeySet()
        {
            return methodValues.keySet();
        }
    }
}
