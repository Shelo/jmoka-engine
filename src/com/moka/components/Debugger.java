package com.moka.components;

import com.moka.core.entity.Component;
import com.moka.core.time.StopWatch;
import com.moka.core.ComponentAttribute;
import com.moka.math.Vector2;
import com.moka.utils.Pools;

public class Debugger extends Component
{
    public enum Options
    {
        NONE,
        POSITION,
        ROTATION,
        SIZE,
    }

    private Options selection = Options.NONE;

    private double frequency = 1;
    private StopWatch stopWatch;

    @Override
    public void onCreate()
    {
        stopWatch = getTime().newStopWatch();
    }

    @Override
    public void onUpdate()
    {
        if (stopWatch.isGreaterThan(frequency))
        {
            stopWatch.restart();
        }
        else
        {
            return;
        }

        switch (selection)
        {
            case POSITION:
                log("position: " + getTransform().getPosition());
                break;

            case ROTATION:
                Vector2 buffer = Pools.vec2.take();
                log("rotation: " + getTransform().getFront(buffer).angle());
                Pools.vec2.put(buffer);
                break;

            case SIZE:
                log("size: " + getTransform().getSize());
                break;
        }
    }

    @ComponentAttribute("option")
    public void setSelection(Options selection)
    {
        this.selection = selection;
    }

    @ComponentAttribute("frequency")
    public void setFrequency(double frequency)
    {
        this.frequency = frequency;
    }
}
