package com.moka.components;

import com.moka.core.Moka;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.time.StopWatch;
import com.moka.triggers.Trigger;

/**
 * Sets up an interval that will call a trigger every X seconds. Note that you can set
 * the time to 0 (zero) in order to call it every frame.
 *
 * @author shelo
 */
public class Interval extends Component
{
    private Trigger<StopWatch> trigger;
    private double time;
    private StopWatch stopWatch;

    @Override
    public void onCreate()
    {
        stopWatch = Moka.getTime().newStopWatch();
    }

    @Override
    public void onUpdate()
    {
        if (stopWatch.isGreaterThan(time))
        {
            trigger.trigger(this, stopWatch);
            stopWatch.restart();
        }
    }

    /**
     * Sets the time of the interval.
     *
     * @param time the time in seconds.
     */
    @ComponentAttribute("Time")
    public void setTime(double time)
    {
        this.time = time;
    }

    /**
     * Sets the trigger (callback) that this interval will call
     * every X seconds.
     *
     * @param trigger the trigger to be called.
     */
    @ComponentAttribute(value = "Trigger", required = true)
    public void setTrigger(Trigger<StopWatch> trigger)
    {
        this.trigger = trigger;
    }
}
