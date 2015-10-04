package com.moka.core;

import com.moka.graphics.Display;
import com.moka.graphics.Renderer;
import com.moka.input.Input;
import com.moka.physics.Physics;
import com.moka.resources.Resources;
import com.moka.scene.Context;
import com.moka.time.Time;
import com.moka.utils.JMokaLog;

/**
 * The Application is the general wrapper of a jMoka game. It contains all sub engines and
 * helper classes in one place.
 *
 * @author shelo
 */
public class Application
{
    private static final String TAG = "Application";

    private Resources resources;
    private NameManager nameManager;
    private Renderer renderer;
    private Context context;
    private Physics physics;
    private Display display;
    private Input input;
    private Core core;
    private Time time;

    private boolean created;

    public Application(Resources resources)
    {
        this.resources = resources;

        nameManager = new NameManager();
        context = new Context();
        renderer = new Renderer();
        physics = new Physics();
        display = new Display();
        input = new Input();
        core = new Core();
        time = new Time();

        exportApplication();
    }

    public Application create()
    {
        // do nothing if this was already created.
        if (created)
            return this;

        renderer.create();
        input.create();

        // load resources.
        resources.internalLoad();

        created = true;

        // create the physics.
        physics.create();

        // create the context.
        context.create();

        JMokaLog.o(TAG, "Created.");
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
        nameManager.setApplication(this);
        renderer.setApplication(this);
        context.setApplication(this);
        physics.setApplication(this);
        display.setApplication(this);
        input.setApplication(this);
        time.setApplication(this);
        core.setApplication(this);
        new Moka(this);
    }

    public boolean isCreated()
    {
        return created;
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

    public NameManager getNameManager()
    {
        return nameManager;
    }

    public Resources getResources()
    {
        return resources;
    }
}
