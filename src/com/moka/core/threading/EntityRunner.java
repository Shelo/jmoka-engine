package com.moka.core.threading;

import com.moka.core.entity.Entity;

public class EntityRunner implements Runnable
{
    protected ActionDelegator delegator;
    protected Entity entity;

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }

    public void setDelegator(ActionDelegator delegator)
    {
        this.delegator = delegator;
    }

    @Override
    public void run()
    {
        delegator.execute(entity, this);
    }
}
