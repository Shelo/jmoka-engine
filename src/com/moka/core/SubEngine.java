package com.moka.core;

import com.moka.input.Input;
import com.moka.scene.Context;
import com.moka.time.Time;
import com.moka.graphics.Display;
import com.moka.graphics.Renderer;
import com.moka.physics.Physics;
import com.moka.utils.JMokaLog;

/**
 * Class designed for internal inheritance, it will give shortcut methods to access other
 * sub engines around the application.
 *
 * @author Shelo
 */
public abstract class SubEngine
{
    protected Application application;

    public void setApplication(Application application)
    {
        this.application = application;
    }

    public Application getApplication()
    {
        return application;
    }

    public Display getDisplay()
    {
        return application.getDisplay();
    }

    public Core getCore()
    {
        return application.getCore();
    }

    public Renderer getRenderer()
    {
        return application.getRenderer();
    }

    public Time getTime()
    {
        return application.getTime();
    }

    public Context getContext()
    {
        return application.getContext();
    }

    public Physics getPhysics()
    {
        return application.getPhysics();
    }

    public Input getInput()
    {
        return application.getInput();
    }

    public NameManager getNameManager()
    {
        return application.getNameManager();
    }

    public void log(Object message)
    {
        JMokaLog.o(this.getClass().getSimpleName(), message.toString());
    }

    public void log(String message)
    {
        JMokaLog.o(this.getClass().getSimpleName(), message);
    }
}
