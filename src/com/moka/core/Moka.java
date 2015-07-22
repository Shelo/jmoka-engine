package com.moka.core;

import com.moka.core.time.Time;
import com.moka.graphics.Display;
import com.moka.graphics.Renderer;
import com.moka.physics.Physics;
import com.moka.utils.JMokaException;

/**
 * The moka interface serves as the definition for the service locator of the engine.
 */
public class Moka
{
    private static Application application;

    protected Moka(Application application)
    {
        if (Moka.application == null)
        {
            Moka.application = application;
        }
        else
        {
            throw new JMokaException("Can't create, the application already exists.");
        }
    }

    public static Context getContext()
    {
        return application.getContext();
    }

    public static Core getCore()
    {
        return application.getCore();
    }

    public static Display getDisplay()
    {
        return application.getDisplay();
    }

    public static Time getTime()
    {
        return application.getTime();
    }

    public static Renderer getRenderer()
    {
        return application.getRenderer();
    }

    public static Physics getPhysics()
    {
        return application.getPhysics();
    }

    public static Input getInput()
    {
        return application.getInput();
    }
}
