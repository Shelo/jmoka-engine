package com.moka.core.threading;

import com.moka.core.entity.Entity;

public abstract class ActionDelegator
{
    public abstract void execute(Entity entity, Runnable runnable);

    public static ActionDelegator update = new ActionDelegator()
    {
        @Override
        public void execute(Entity entity, Runnable runnable)
        {
            entity.update();
            Threading.put((EntityRunner) runnable);
        }
    };
}
