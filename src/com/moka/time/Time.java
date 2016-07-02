package com.moka.time;

import com.moka.core.SubEngine;
import com.moka.scene.entity.Component;
import com.moka.triggers.Trigger;

import java.util.ArrayList;

public class Time extends SubEngine
{
    private ArrayList<StopWatch> stopWatches = new ArrayList<>();

    private double elapsed;
    private double delta;

    public void update(double delta)
    {
        this.delta = delta;
        elapsed += delta;

        for (int i = stopWatches.size() - 1; i >= 0; i--) {
            if (stopWatches.get(i).shouldDestroy()) {
                stopWatches.remove(i);
            } else {
                stopWatches.get(i).update(delta);
            }
        }
    }

    /**
     * The delta is the time passed since the last frame. It is expressed in seconds and makes
     * for a great way to synchronize events even with frame drops.
     *
     * Usage:
     * <code>
     * getTransform().move(100 * getDelta(), 0);
     * </code>
     *
     * This will effectively move the entity 100 pixels to the right, per second.
     *
     * @return the current delta time.
     */
    public float getDelta()
    {
        return (float) delta;
    }

    public float getFixedDelta()
    {
        return (float) delta;
    }

    public double getElapsed()
    {
        return elapsed;
    }

    public TimeOut newTimeOut(Component component, float seconds, Trigger trigger)
    {
        TimeOut timeOut = new TimeOut(component, seconds, trigger);
        stopWatches.add(timeOut);

        return timeOut;
    }

    public StopWatch newStopWatch()
    {
        StopWatch stopWatch = new StopWatch();
        stopWatches.add(stopWatch);

        return stopWatch;
    }
}
