package com.moka.core.time;

import com.moka.core.entity.Component;
import com.moka.triggers.Trigger;

/**
 * A timer is an interval, it will call a trigger every x seconds.
 * @param <T> the type for the trigger that this will call.
 */
public class Timer<T> extends StopWatch
{
    private Component component;
    private Trigger<T> trigger;
    private boolean death;
    private float time;

    Timer(Component component, float time, Trigger<T> trigger)
    {
        this.component = component;
        this.trigger = trigger;
        this.time = time;
    }

    @Override
    void update(double delta)
    {
        super.update(delta);

        if (getElapsed() > time)
        {
            if (trigger != null)
            {
                Boolean response = (Boolean) trigger.trigger(component, null);

                if (response != null && response)
                {
                    death = true;
                }
                else
                {
                    restart();
                }
            }
        }
    }

    public boolean isDeath()
    {
        return death;
    }
}
