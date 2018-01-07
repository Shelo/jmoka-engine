package com.moka.core.threading;

import com.moka.scene.entity.Entity;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Threading
{
    private static Queue<EntityRunner> runners = new LinkedBlockingQueue<>();

    public static Runnable runnable(ActionDelegate delegator, Entity entity)
    {
        EntityRunner runner = runners.poll();

        if (runner == null) {
            runner = new EntityRunner();
        }

        runner.setEntity(entity);
        runner.setDelegate(delegator);

        return runner;
    }

    public static void put(EntityRunner entityRunner)
    {
        runners.add(entityRunner);
    }
}
