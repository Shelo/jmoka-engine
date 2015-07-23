package com.moka.core;

import com.moka.core.time.Time;
import com.moka.graphics.Display;
import com.moka.graphics.Renderer;
import com.moka.physics.Physics;
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

    private MokaResources resources;
    private NameManager nameManager;
    private Renderer renderer;
    private Context context;
    private Physics physics;
    private Display display;
    private Input input;
    private Core core;
    private Time time;

    private boolean created;

    public Application(MokaResources resources)
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
        {
            return this;
        }

        renderer.create();
        input.create();

        // load resources.
        resources.load();

        created = true;

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
    protected Context getContext()
    {
        return context;
    }

    protected Core getCore()
    {
        return core;
    }

    protected Display getDisplay()
    {
        return display;
    }

    protected Time getTime()
    {
        return time;
    }

    protected Renderer getRenderer()
    {
        return renderer;
    }

    protected Physics getPhysics()
    {
        return physics;
    }

    protected Input getInput()
    {
        return input;
    }

    protected NameManager getNameManager()
    {
        return nameManager;
    }

    protected MokaResources getResources()
    {
        return resources;
    }
}
