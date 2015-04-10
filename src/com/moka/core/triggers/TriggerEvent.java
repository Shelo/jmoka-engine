package com.moka.core.triggers;

import com.moka.components.Component;
import com.moka.core.Entity;

/**
 * Defines a simple definition for an event.
 *
 * @param <T> a custom type for the trigger.
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
     * @return the component.
     */
    public Component getComponent()
    {
        return component;
    }

    /**
     * A shortcut method to get the entity. This is potentially a very common operation.
     * @return the entity holding the component.
     */
    public Entity getEntity()
    {
        return component.getEntity();
    }
}
