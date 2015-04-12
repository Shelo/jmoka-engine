package com.moka.graphics;

import com.moka.core.SubEngine;
import com.moka.utils.JMokaException;
import com.moka.utils.JMokaLog;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;

public final class Display extends SubEngine
{
    private static final String TAG = "DISPLAY";

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
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        // center window.
        ByteBuffer vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (GLFWvidmode.width(vidMode) - width) / 2, (GLFWvidmode.height(vidMode) - height) / 2);

        if (window == 0)
        {
            throw new JMokaException("Window could not be created.");
        }
        else
        {
            JMokaLog.o(TAG, "Window created.");
        }

        glfwMakeContextCurrent(window);

        // this is a critical line!!
        GLContext.createFromCurrent();
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

    public String getTitle()
    {
        return title;
    }
}
