package com.moka.triggers;

import com.moka.core.Application;
import com.moka.core.Component;
import com.moka.core.Entity;
import com.moka.core.Transform;
import com.moka.utils.JMokaException;
import com.moka.utils.JMokaLog;

import java.lang.reflect.Field;

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
        if (component == null)
        {
            throw new JMokaException("Cannot pass null to the component parameter.");
        }

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

    public T getMeta()
    {
        return meta;
    }

    public Application getApplication()
    {
        return component.getApplication();
    }

    public Transform getTransform()
    {
        return component.getTransform();
    }

    /**
     * Logs a descriptive message to the console.
     */
    public void log(String message)
    {
        String tag = getEntity().getName() + " -> " + getComponent().getClass().getSimpleName() + " -> Trigger";
        JMokaLog.o(tag, message);
    }

    /**
     * Gets the static trigger for a given path and a type.
     *
     * @param path      the path to the static trigger.
     * @param generic   the generic type of the meta.
     * @return the trigger.
     */
    @SuppressWarnings("unchecked")
    public static <T> Trigger<T> getStaticTrigger(String path, Class<T> generic) {
        int divider = path.lastIndexOf('.');
        String classPath = path.substring(0, divider);
        String triggerName = path.substring(divider + 1);

        try
        {
            Class<?> triggerClass = Class.forName(classPath);
            Field field = triggerClass.getDeclaredField(triggerName);
            return (Trigger<T>) field.get(null);
        }
        catch (ClassNotFoundException e)
        {
            throw new JMokaException("Class not found for that trigger.");
        }
        catch (NoSuchFieldException e)
        {
            throw new JMokaException("The trigger: " + path + " doesn't exists within the class.");
        }
        catch (IllegalAccessException e)
        {
            throw new JMokaException("Cannot access the trigger.");
        }
    }
}
