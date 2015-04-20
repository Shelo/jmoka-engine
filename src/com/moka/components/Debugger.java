package com.moka.components;

import com.moka.core.Component;
import com.moka.core.Timer;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2f;

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
    private Vector2f buffer = new Vector2f();

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
                log("rotation: " + getTransform().getFront(buffer).angle());
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
