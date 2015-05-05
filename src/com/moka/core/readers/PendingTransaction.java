package com.moka.core.readers;

import com.moka.core.entity.Component;

import java.lang.reflect.Method;

/**
 * This pending transaction is used to analyze some attributes later while we can resolve
 * the non existing dependencies. An example will be that we reference an entity that doesn't
 * exists till now, but it might.
 *
 * @author shelo
 */
public class PendingTransaction
{
    private Component component;
    private Method method;
    private String value;

    public PendingTransaction(Component component, Method method, String value)
    {
        this.component = component;
        this.method = method;
        this.value = value;
    }

    public Component getComponent()
    {
        return component;
    }

    public Method getMethod()
    {
        return method;
    }

    public String getValue()
    {
        return value;
    }
}
