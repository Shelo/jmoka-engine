package com.moka.time;

import com.moka.core.entity.Component;
import com.moka.triggers.Trigger;

/**
 * A timer is an interval, it will call a trigger every x seconds.
 */
public class TimeOut extends StopWatch
{
    private Component component;
    private Trigger trigger;
    private float time;

    TimeOut(Component component, float seconds, Trigger trigger)
    {
        this.component = component;
        this.trigger = trigger;
        this.time = seconds;
    }

    @Override
    void update(double delta)
    {
        time -= delta;

        if (time <= 0)
        {
            if (trigger != null)
            {
                trigger.trigger(component, null);
            }
        }
    }

    public void cancel()
    {
        time = 0;
    }

    @Override
    public boolean shouldDestroy()
    {
        return time <= 0;
    }
}
