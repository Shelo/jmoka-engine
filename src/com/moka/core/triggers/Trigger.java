package com.moka.core.triggers;

import com.moka.utils.JMokaException;

import java.lang.reflect.Field;

/**
 * Defines a simple trigger method for use in a component. Components should use this to trigger actions,
 * see "delegation pattern".
 *
 * @author shelo
 */
public abstract class Trigger<T>
{
    /**
     * Triggers an action for a given component. To know what the parameter does,
     * see {@link TriggerEvent}.
     *
     * @param event the event sent by the caller.
     * @return true if the action was successfully handled.
     */
    public abstract boolean onTrigger(TriggerEvent<T> event);

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
            throw new JMokaException("The trigger doesn't exists within the class.");
        }
        catch (IllegalAccessException e)
        {
            throw new JMokaException("Cannot access the trigger.");
        }
    }
}
