package com.moka.components;

import com.moka.core.Timer;
import com.moka.core.triggers.Trigger;
import com.moka.core.triggers.TriggerEvent;
import com.moka.core.xml.XmlAttribute;

/**
 * Sets up an interval that will call a trigger every X seconds. Note that you can set
 * the time to 0 (zero) in order to call it every frame.
 *
 * @author shelo
 */
public class Interval extends Component
{
    private Trigger<Timer> trigger;
    private double time;
    private Timer timer;

    @Override
    public void onCreate()
    {
        timer = getTime().newTimer();
    }

    @Override
    public void onUpdate()
    {
        if (timer.isGreaterThan(time))
        {
            trigger.onTrigger(new TriggerEvent<Timer>(this, timer));
            timer.restart();
        }
    }

    /**
     * Sets the time of the interval.
     *
     * @param time the time in seconds.
     */
    @XmlAttribute("time")
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
    @XmlAttribute(value = "trigger", required = true)
    public void setTrigger(Trigger<Timer> trigger)
    {
        this.trigger = trigger;
    }
}
