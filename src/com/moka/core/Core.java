package com.moka.core;

import com.moka.utils.JMokaException;
import com.moka.utils.JMokaLog;
import org.xml.sax.SAXParseException;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Core of the engine, takes care of the game loop and triggers.
 *
 * @author shelo
 */
public final class Core extends SubEngine
{
    private static final String TAG = "CORE";

    private ChangeWatcher watcher;
    private boolean experimental;
    private float frameTime;
    private boolean daemon;
    private Runtime runtime;
    private boolean watch;

    /**
     * Creates a new instance of the engine.
     */
    public Core()
    {
        if (glfwInit() == 0)
        {
            throw new JMokaException("Error initializing GLFW.");
        }
        else
        {
            JMokaLog.o(TAG, "GLFW initialized.");
        }

        runtime = Runtime.getRuntime();
        watcher = new ChangeWatcher("res/scene");
    }

    public void start(int maxFrameRate)
    {
        if (maxFrameRate == 0)
        {
            throw new JMokaException("MaxFrameTime cannot be zero.");
        }

        frameTime = 1.0f / maxFrameRate;

        if (daemon)
        {
            return;
        }

        daemon = true;
        run();
    }

    public void run()
    {
        try
        {
            JMokaLog.o(TAG, "Stabilizing for 500ms...");
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // fixed delta time.
        double delta = frameTime;

        // current time var and accumulator.
        double currentTime = glfwGetTime();
        double accumulator = 0.0;

        // this is just to show some statics.
        int updateFrames = 0;
        int renderFrames = 0;
        double accSeconds = 0;

        while (daemon)
        {
            // indicates if we should render or not.
            boolean render = false;

            // get the time that the render frame lasted.
            double newTime = glfwGetTime();
            double frameTime = newTime - currentTime;
            currentTime = newTime;

            // increase the accumulator by this frame time.
            accumulator += frameTime;

            // "spend" the accumulator time until is lower than the delta time.
            // while we have time accumulated left, we onUpdate the scene.
            while (accumulator > delta)
            {
                // we'll need to render only if we update the scene... obviously.
                render = true;

                if (watch && (experimental || getDisplay().hasFocus()))
                {
                    if (watcher.hasChanges())
                    {
                        StringBuilder lastMessage = new StringBuilder();
                        boolean notUpdated = true;
                        while (notUpdated)
                        {
                            try
                            {
                                getContext().hardReset();
                                getContext().onCreate();
                                getContext().create();
                                notUpdated = false;
                            }
                            catch (JMokaException e)
                            {
                                String message = "Could not update: " + e.getMessage();
                                if (!lastMessage.toString().equals(message))
                                {
                                    lastMessage.append("Could not update: ");
                                    lastMessage.append(e.getMessage());
                                    System.err.println(message);
                                }

                                try
                                {
                                    Thread.sleep(100);
                                }
                                catch (InterruptedException e1)
                                {
                                    // Left blank.
                                }
                            }
                        }
                    }
                }

                getTime().update(delta);
                getContext().update();
                getContext().onUpdate();
                getPhysics().checkCollisions();
                getContext().postUpdate();
                getContext().clean();
                getInput().update();

                // check if user closed the window.
                if (getDisplay().isCloseRequested())
                {
                    daemon = false;
                }

                // subtract used delta time and add it to the game time.
                accumulator -= delta;
                accSeconds += delta;

                updateFrames++;
            }

            // when the accumulator is over, render and onUpdateAll the display.
            if (render)
            {
                renderFrames++;
                getRenderer().render();
                getDisplay().onUpdate();
            }
            else
            {
                try
                {
                    Thread.sleep(1);
                }
                catch (InterruptedException e)
                {
                    // Does nothing.
                }
            }

            if (accSeconds >= 1)
            {
                float usedMemory = ((runtime.totalMemory() - runtime.freeMemory()) / (1024.0f * 1024.0f));
                JMokaLog.o(TAG, renderFrames + " fps, " + updateFrames + " ups. Used Memory: " + usedMemory + "MB");
                accSeconds = renderFrames = updateFrames = 0;
            }
        }

        stop();
    }

    public void stop()
    {
        JMokaLog.o(TAG, "Stopping JMoka Engine.");
        daemon = false;
        getContext().dispose();
        glfwTerminate();
    }

    public void enableFeedback()
    {
        watch = true;
    }

    public void enableExperimentalFeedback()
    {
        watch = true;
        experimental = true;
    }
}
