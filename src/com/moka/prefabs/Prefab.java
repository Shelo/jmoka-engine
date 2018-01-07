package com.moka.prefabs;

import com.moka.core.Moka;
import com.moka.math.Vector2;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.scene.entity.Entity;
import com.moka.scene.entity.Transform;
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
    private int layer = 1;

    /**
     * Creates a new Prefab for a given context with given components.
     *
     * @param components components.
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
     * Sets the position of the prefab to the position vector and then instantiates.
     *
     * @param name     name for the new instance.
     * @param position position for the new instance.
     * @return the new getEntity.
     */
    public Entity newEntity(String name, Vector2 position)
    {
        setPosition(position);
        return newEntity(name);
    }

    /**
     * Sets the position of the prefab to the x, y values and then instantiates.
     *
     * @param name name for the new instance.
     * @param x    x position on the world.
     * @param y    y position on the world.
     * @return the new getEntity.
     */
    public Entity newEntity(String name, float x, float y)
    {
        float ox = position.x;
        float oy = position.y;

        setPosition(x, y);
        Entity entity = newEntity(name);
        setPosition(ox, oy);
        return entity;
    }

    /**
     * Sets the position of the prefab to the x, y values and then instantiates.
     *
     * @param name  name for the new instance.
     * @param layer layer for the new instance.
     * @return the new getEntity.
     */
    public Entity newEntity(String name, int layer)
    {
        setLayer(layer);
        return newEntity(name);
    }

    /**
     * Creates a new instance with using this prefab states.
     *
     * @param name unique name for the new instance.
     * @param addToScene should the entity be added to the scene automatically.
     * @return the getEntity.
     */
    public Entity newEntity(String name, boolean addToScene)
    {
        Entity entity;

        if (addToScene) {
            entity = Moka.getContext().getCurrentScene().newEntity(name, layer);
        } else {
            entity = new Entity(name);
        }

        entity.setGroup(group);

        // set transform values for this getEntity.
        Transform transform = entity.getTransform();
        transform.setPosition(position.cpy());
        transform.setRotationRadians(rotation);

        if (useOwnSize) {
            transform.setSize(size.cpy());
        }

        // creates every component and adds it to the getEntity.
        for (Class<?> cClass : components.keySet()) {
            ComponentAttrs componentAttrs = components.get(cClass);
            Component component;

            try {
                component = (Component) cClass.newInstance();
                entity.addComponent(component);

                // iterate over the methods and attributes of the component.
                for (Method method : componentAttrs.getKeySet()) {
                    Object[] attr = componentAttrs.getValue(method);

                    if (attr == null) {
                        throw new JMokaException("Some error happened... Maybe the resource is null at this time?");
                    }

                    for (int i = 0; i < attr.length; i++) {
                        // if the attr[i] is an instance of a trigger, then get a new instance of that trigger
                        // in order to pass it to the setter method.
                        if (attr[i] != null && attr[i].getClass().isAssignableFrom(TriggerPromise.class))
                            attr[i] = Trigger.newTriggerInstance(((TriggerPromise) attr[i]).getTriggerClass());
                    }

                    try {
                        method.invoke(component, attr);
                    } catch (InvocationTargetException e) {
                        throw new JMokaException("Cannot set the attribute '"
                                + method.getAnnotation(ComponentAttribute.class).value() + "' because: "
                                + e.getTargetException().getMessage());
                    }
                }
            } catch (InstantiationException e) {
                throw new JMokaException("Cannot instantiate the component " + cClass.getName()
                        + ", maybe there's no non-args constructor.");
            } catch (IllegalAccessException e) {
                throw new JMokaException("Cannot access the component " + cClass.getName() + ".");
            }
        }

        // after this we should call the onCreate method on the components.
        if (Moka.getContext().getCurrentScene().isCreated())
            entity.create();

        return entity;
    }

    /**
     * Creates a new instance with using this prefab states. The instance will
     * be automatically added to the stage.
     *
     * @param name unique name for the new instance.
     * @return the getEntity.
     */
    public Entity newEntity(String name)
    {
        return newEntity(name, true);
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    /**
     * This is just a shortcut so I don't have to write that big HashMap every time.
     */
    public static class PreComponents extends HashMap<Class<?>, ComponentAttrs>
    {

    }

    /**
     * Wrapper and helper class for the prefab.
     */
    public static class ComponentAttrs
    {
        private HashMap<Method, Object[]> methodValues = new HashMap<>();

        public void addMethodValues(Method method, Object[] value)
        {
            methodValues.put(method, value);
        }

        public Object[] getValue(Method name)
        {
            return methodValues.get(name);
        }

        public Set<Method> getKeySet()
        {
            return methodValues.keySet();
        }
    }
}
