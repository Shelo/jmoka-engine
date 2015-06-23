package com.moka.triggers;

public class TriggerPromise
{
    private Class<?> triggerClass;

    public TriggerPromise(Class<?> clazz)
    {
        triggerClass = clazz;
    }

    public Class<?> getTriggerClass()
    {
        return triggerClass;
    }
}
