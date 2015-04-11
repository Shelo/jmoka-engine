package com.moka.core.triggers;

import com.moka.components.Component;
import com.moka.core.Entity;
import com.moka.core.Transform;

/**
 * Defines a simple definition for an event.
 * It contains the component and a meta data. That data could be anything,
 * for example, if a component need to send a certain position to the trigger,
 * the we can say TriggerEvent<Vector2f> to actually send the position.
 *
 * There's no limit to what can be sent, use a HashMap if you need a lot of
 * parameters or maybe define a simple Wrapper.
 *
 * Also, this event contains a series of shortcut methods that are very
 * commonly used: get the entity, get the transform, etc.
 *
 * @param <T> a custom type for the trigger.
 *
 * @author shelo
 */
public class TriggerEvent<T>
{
    private Component component;
    private T meta;

    /**
     * Creates a new trigger event with a component and a meta data.
     *
     * @param component the component that triggered the event.
     * @param meta some metadata to be sent.
     */
    public TriggerEvent(Component component, T meta)
    {
        this.component = component;
        this.meta = meta;
    }

    /**
     * Returns the meta data of the event.
     *
     * @return meta data.
     */
    public T getMeta()
    {
        return meta;
    }

    /**
     * Returns the component that triggered the event.
     *
     * @return the component.
     */
    public Component getComponent()
    {
        return component;
    }

    /**
     * A shortcut method to get the entity. This is potentially a very common operation.
     *
     * @return the entity holding the component.
     */
    public Entity getEntity()
    {
        return component.getEntity();
    }

    /**
     * Gets the transform of the entity that has the component.
     *
     * @return the transform.
     */
    public Transform getTransform()
    {
        return component.getTransform();
    }
}
