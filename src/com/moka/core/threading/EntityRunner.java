package com.moka.core.threading;

import com.moka.scene.entity.Entity;

public class EntityRunner implements Runnable
{
    protected ActionDelegate delegate;
    protected Entity entity;

    public void setEntity(Entity entity)
    {
        this.entity = entity;
    }

    public void setDelegate(ActionDelegate delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public void run()
    {
        delegate.execute(entity, this);
    }
}
