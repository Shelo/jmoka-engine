package com.moka.triggers;

public class TriggerPrefab
{
    private Class<?> triggerClass;

    public TriggerPrefab(Class<?> clazz)
    {
        triggerClass = clazz;
    }

    public Class<?> getTriggerClass()
    {
        return triggerClass;
    }
}
