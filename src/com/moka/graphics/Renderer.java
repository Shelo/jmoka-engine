package com.moka.graphics;

import com.moka.components.Camera;
import com.moka.components.Sprite;
import com.moka.core.SubEngine;
import com.moka.scene.entity.Entity;
import com.moka.utils.JMokaException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

public final class Renderer extends SubEngine
{
    public static final String VERTEX_CODE =
            "#version 330 core\n" +
            "\n" +
            "layout (location = 0) in vec2 a_position;\n" +
            "layout (location = 1) in vec2 a_texCoord;\n" +
            "\n" +
            "uniform mat3 u_projectedView;\n" +
            "uniform mat3 u_model;\n" +
            "\n" +
            "out vec2 texCoord;\n" +
            "\n" +
            "void main() {\n" +
            "\tvec3 position = u_projectedView * (u_model * vec3(a_position, 1.0));\n" +
            "\tgl_Position = vec4(position.xy, 0, position.z);\n" +
            "\ttexCoord = a_texCoord;\n" +
            "}";

    public static final String FRAGMENT_CODE =
            "#version 330 core\n" +
            "\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform vec4 u_color;\n" +
            "\n" +
            "in vec2 texCoord;\n" +
            "\n" +
            "out vec4 fragColor;\n" +
            "\n" +
            "void main() {\n" +
            "\tvec4 baseColor = texture(u_texture, texCoord);\n" +
            "\n" +
            "\tfragColor = baseColor * u_color;\n" +
            "}\n";

    private Shader defaultShader;
    private Shader shader;
    private Camera camera;
    private int currentShaderProgram = -1;
    private int currentTextureId = -1;

    private Color clearColor = new Color(0, 0, 0, 1);

    /**
     * Creates the Renderer. This will initialize some OpenGL constants and create the shader.
     */
    public void create()
    {
        // create shader.
        defaultShader = new Shader(VERTEX_CODE, FRAGMENT_CODE);
        setShader(null);

        // initialize OpenGL stuff.
        updateClearColor();

        glFrontFace(GL_CW);

        // enable culling for better performance.
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // enable blending.
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_TEXTURE_2D);

        log("Created correctly");
    }

    /**
     * Render this frame. At this point the camera cannot be null.
     */
    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (camera == null)
            throw new JMokaException("There's no camera attached to the renderer.");

        shader.bind();
        shader.setUniform("u_projectedView", camera.getProjectedView());

        for (Entity entity : getContext().getCurrentScene())
        {
            if (entity.hasSprite())
            {
                Sprite sprite = entity.getSprite();

                if (sprite.isEnabled())
                    sprite.render(shader);
            }
        }
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

        if (getApplication().isCreated())
            updateClearColor();
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
            throw new JMokaException("Renderer.setCurrentCamera: the given camera cannot be null.");

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

    /**
     * Sets the shader to use. If the shader is null, this restores to the default shader.
     *
     * @param shader the shader program.
     */
    public void setShader(Shader shader)
    {
        if (shader == null)
            this.shader = defaultShader;
        else
            this.shader = shader;
    }

    public void bindTexture(int textureId)
    {
        if (currentTextureId != textureId)
        {
            glBindTexture(GL_TEXTURE_2D, textureId);
            currentTextureId = textureId;
        }
    }

    public void bindShader(int shaderProgram)
    {
        if (shaderProgram != currentShaderProgram)
        {
            glUseProgram(shaderProgram);
            currentShaderProgram = shaderProgram;
        }
    }
}
