package com.moka.graphics;

import com.moka.core.SubEngine;
import com.moka.math.Vector2;
import com.moka.utils.JMokaException;
import com.moka.utils.JMokaLog;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public final class Display extends SubEngine
{
    private static final String TAG = "DISPLAY";

    private WindowsFocusCallback windowsFocusCallback;

    private boolean focus;
    private String title;
    private long window;
    private int height;
    private int width;

    public void createDisplay(int width, int height, String title)
    {
        this.height = height;
        this.width = width;
        this.title = title;

        glfwWindowHint(GLFW_SAMPLES, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        if (window == 0)
        {
            throw new JMokaException("Window could not be created.");
        }
        else
        {
            JMokaLog.o(TAG, "Window created.");
        }

        // center window.
        ByteBuffer vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window,(GLFWvidmode.width(vidMode) - width) / 2,
                (GLFWvidmode.height(vidMode) - height) / 2);

        glfwMakeContextCurrent(window);

        // this is a critical line!!
        GLContext.createFromCurrent();

        // set the focus gain or lose callback.
        windowsFocusCallback = new WindowsFocusCallback();
        glfwSetWindowFocusCallback(window, windowsFocusCallback);
    }

    public void createDisplay(String widthRes, String heightRes, String title)
    {
        // get width and height from resources.
        int width = getResources().getInt(widthRes);
        int height = getResources().getInt(heightRes);

        createDisplay(width, height, title);
    }

    public void start()
    {
        glfwShowWindow(window);
    }

    @Deprecated
    public void setIcon()
    {
        // TODO: LWJGL doesn't support icons for now.
    }

    public void onUpdate()
    {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public boolean isCloseRequested()
    {
        return glfwWindowShouldClose(window) != 0;
    }

    public boolean hasFocus()
    {
        return focus;
    }

    /**
     * This is a simple workaround for the focus issue of GLFW.
     */
    public void fixFocus()
    {
        focus = true;
    }

    public long getWindow()
    {
        return window;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Vector2 getSizeVector()
    {
        return new Vector2(width, height);
    }

    public String getTitle()
    {
        return title;
    }

    /**
     * Callback for windows focus.
     */
    private class WindowsFocusCallback extends GLFWWindowFocusCallback
    {
        @Override
        public void invoke(long l, int i)
        {
            focus = i != 0;
        }
    }
}
