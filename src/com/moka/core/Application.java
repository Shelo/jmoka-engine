package com.moka.core;

import com.moka.core.contexts.Context;
import com.moka.core.time.Time;
import com.moka.graphics.Display;
import com.moka.graphics.Renderer;
import com.moka.physics.Physics;

/**
 * The Application is the general wrapper of a jMoka game. It contains all sub engines and
 * helper classes in one place.
 *
 * @author shelo
 */
public class Application
{
    private Resources resources;
    private Renderer renderer;
    private Context context;
    private Physics physics;
    private Display display;
    private Input input;
    private Core core;
    private Time time;

    private boolean created;

    public Application()
    {
        resources = new Resources();
        renderer = new Renderer();
        physics = new Physics();
        display = new Display();
        input = new Input();
        core = new Core();
        time = new Time();

        exportApplication();
    }

    public void setContext(Context context)
    {
        this.context = context;
        context.setApplication(this);
        context.onPreLoad();
    }

    public Application create()
    {
        // do nothing if this was already created.
        if (created)
        {
            return this;
        }

        renderer.create();
        input.create();

        created = true;

        // lets the context add all entities and components.
        context.onCreate();

        // create all components.
        context.create();

        return this;
    }

    public void start(int maxFrameRate)
    {
        display.start();
        core.start(maxFrameRate);
    }

    /**
     * Sets this is the application for all sub engines.
     */
    private void exportApplication()
    {
        resources.setApplication(this);
        renderer.setApplication(this);
        physics.setApplication(this);
        display.setApplication(this);
        input.setApplication(this);
        time.setApplication(this);
        core.setApplication(this);
    }

    // GETTERS AND SETTERS.
    public Context getContext()
    {
        return context;
    }

    public Core getCore()
    {
        return core;
    }

    public Display getDisplay()
    {
        return display;
    }

    public Resources getResources()
    {
        return resources;
    }

    public Time getTime()
    {
        return time;
    }

    public Renderer getRenderer()
    {
        return renderer;
    }

    public Physics getPhysics()
    {
        return physics;
    }

    public Input getInput()
    {
        return input;
    }
}
