package com.moka.graphics;

import com.moka.components.Camera;
import com.moka.core.SubEngine;
import com.moka.utils.JMokaException;

import static org.lwjgl.opengl.GL11.*;

public final class Renderer extends SubEngine
{
    public static final String SHADERS_PATH     = "res/shaders/";
    public static final String VERTEX_SHADER    = "pass_through_vertex.glsl";
    public static final String FRAGMENT_SHADER  = "pass_through_fragment.glsl";

    private Camera camera;
    private Shader shader;

    private Color clearColor = new Color(0, 0, 0, 1);

    /**
     * Creates the Renderer. This will initialize some OpenGL constants and create the shader.
     */
    public void create()
    {
        // create shader.
        shader = new Shader(SHADERS_PATH + VERTEX_SHADER, SHADERS_PATH + FRAGMENT_SHADER);

        // initialize OpenGL stuff.
        updateClearColor();

        glFrontFace(GL_CW);

        // enable culling for better performance.
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // enable blending.
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Render this frame. At this point the camera cannot be null.
     */
    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        shader.bind();

        if (camera == null)
        {
            throw new JMokaException("There's no camera attached to the renderer.");
        }

        shader.setUniform("u_projectedView", camera.getProjectedViewMatrix());

        getContext().render(shader);
    }

    /**
     * Change the clear color. This will not work if the application was already created, in order
     * to trigger a change after that use updateClearColor method.
     *
     * @param r the red component of the color.
     * @param g the green component of the color.
     * @param b the blue component of the color.
     */
    public void setClearColor(float r, float g, float b)
    {
        this.clearColor.r = r;
        this.clearColor.g = g;
        this.clearColor.b = b;
    }

    /**
     * Updates the clear color in runtime.
     */
    public void updateClearColor()
    {
        glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
    }

    /**
     * Returns the camera used for the renderer at this instant.
     *
     * @return the main camera.
     */
    public Camera getCamera()
    {
        return camera;
    }

    /**
     * Sets the main camera for the renderer to use.
     *
     * @param camera the new main camera, null will crash the engine.
     */
    public void setCamera(Camera camera)
    {
        if (camera == null)
        {
            throw new JMokaException("Renderer.setCurrentCamera: the given camera cannot be null.");
        }

        this.camera = camera;
    }

    /**
     * Returns the current shader program.
     *
     * @return the shader.
     */
    public Shader getShader()
    {
        return shader;
    }
}
