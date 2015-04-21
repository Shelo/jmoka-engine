package com.moka.components;

import com.moka.core.Component;
import com.moka.core.Timer;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2;
import com.moka.utils.pools.Vector2Pool;

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
    private Timer timer;

    @Override
    public void onCreate()
    {
        timer = getTime().newTimer();
    }

    @Override
    public void onUpdate()
    {
        if (timer.isGreaterThan(frequency))
        {
            timer.restart();
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
                Vector2 buffer = Vector2Pool.take();

                log("rotation: " + getTransform().getFront(buffer).angle());

                Vector2Pool.put(buffer);
                break;

            case SIZE:
                log("size: " + getTransform().getSize());
                break;
        }
    }

    @XmlAttribute("option")
    public void setSelection(Options selection)
    {
        this.selection = selection;
    }

    @XmlAttribute("frequency")
    public void setFrequency(double frequency)
    {
        this.frequency = frequency;
    }
}
