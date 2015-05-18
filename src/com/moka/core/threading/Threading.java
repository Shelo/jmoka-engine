package com.moka.core.threading;

import com.moka.core.entity.Entity;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Threading
{
    private static Queue<EntityRunner> runners = new LinkedBlockingQueue<>();

    public static Runnable runnable(ActionDelegator delegator, Entity entity)
    {
        EntityRunner runner = runners.poll();

        if (runner == null)
        {
            runner = new EntityRunner();
        }

        runner.setEntity(entity);
        runner.setDelegator(delegator);

        return runner;
    }

    public static void put(EntityRunner entityRunner)
    {
        runners.add(entityRunner);
    }
}
