package com.moka.core.threading;

import com.moka.scene.entity.Entity;

public abstract class ActionDelegate
{
    public abstract void execute(Entity entity, Runnable runnable);

    public static ActionDelegate update = new ActionDelegate()
    {
        @Override
        public void execute(Entity entity, Runnable runnable)
        {
            entity.update();
            Threading.put((EntityRunner) runnable);
        }
    };
}
