package com.moka.triggers;

import com.moka.core.entity.Component;
import com.moka.core.entity.Entity;
import com.moka.core.Transform;
import com.moka.utils.JMokaException;
import com.moka.utils.JMokaLog;

/**
 * Defines a simple trigger method for use in a component. Components should use this to trigger actions,
 * see "delegation pattern". The trigger can hold a meta data, that is just additional information for
 * the trigger.
 *
 * @author shelo
 */
public abstract class Trigger<T>
{
    private Component component;
    private T meta;

    /**
     * Calls the trigger and stores the component and meta data.
     *
     * @param component the caller component.
     * @param meta the meta data.
     *
     * @return an optional information object given by the trigger.
     */
    public Object trigger(final Component component, final T meta)
    {
        this.component = component;
        this.meta = meta;
        return onTrigger();
    }

    /**
     * Triggers the action called by the component.
     *
     * @return an information object if needed.
     */
    public abstract Object onTrigger();

    public Component getComponent()
    {
        return component;
    }

    public Entity getEntity()
    {
        return component.getEntity();
    }

    public T meta()
    {
        return meta;
    }

    public Transform transform()
    {
        return component.getTransform();
    }

    /**
     * Logs a descriptive message to the console.
     */
    public void log(String message)
    {
        String tag = getEntity().getName() + " -> " + getComponent().getClass().getSimpleName() + " -> "
                + getClass().getSimpleName();
        JMokaLog.o(tag, message);
    }

    /**
     * Gets the static trigger for a given path and a type.
     *
     * @param path      the path to the static trigger.
     * @param generic   the generic type of the meta.
     * @return the trigger.
     */
    public static <T> Trigger<T> getStaticTrigger(String path, Class<T> generic)
    {
        return getNewTriggerInstance(getStaticTriggerClass(path, generic));
    }

    @SuppressWarnings("unchecked")
    public static <T> Trigger<T> getNewTriggerInstance(Class<?> triggerClass)
    {
        Object instance = null;

        try
        {
            instance = triggerClass.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new JMokaException("Cannot instantiate the trigger.");
        }
        catch (IllegalAccessException e)
        {
            throw new JMokaException("Cannot access the trigger.");
        }

        return (Trigger<T>) instance;
    }

    public static <T> Class<?> getStaticTriggerClass(String path, Class<T> generic)
    {
        int divider = path.lastIndexOf('.');
        String classPath = path.substring(0, divider);
        String triggerName = path.substring(divider + 1);

        Class<?> trigger = null;

        try
        {
            Class<?> triggerClass = Class.forName(classPath);
            for (Class<?> innerClass : triggerClass.getDeclaredClasses())
            {
                if (innerClass.getSimpleName().equals(triggerName))
                {
                    trigger = innerClass;
                    break;
                }
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        if (trigger == null)
        {
            throw new JMokaException("Could not find the trigger " + triggerName + " in " + classPath);
        }

        return trigger;
    }
}
