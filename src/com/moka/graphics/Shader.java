package com.moka.graphics;

import com.moka.components.Sprite;
import com.moka.core.CoreUtils;
import com.moka.core.JMokaLog;
import com.moka.core.Transform;
import com.moka.core.Utils;
import com.moka.exceptions.JMokaException;
import com.moka.math.Matrix4;
import com.moka.math.Vector2f;
import com.moka.math.Vector3f;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
    public static final String TAG = "SHADER";

    private HashMap<String, Integer> uniformLocations;
    private int fragment;
    private int program;
    private int vertex;

    public Shader(String vertexFilePath, String fragmentFilePath)
    {
        program = glCreateProgram();
        vertex = createShader(vertexFilePath, GL_VERTEX_SHADER);
        fragment = createShader(fragmentFilePath, GL_FRAGMENT_SHADER);

        glBindAttribLocation(program, 0, "a_position");
        glBindAttribLocation(program, 1, "a_texCoord");

        // Link and check errors.
        glLinkProgram(program);

        if (glGetProgrami(program, GL_LINK_STATUS) == 0)
        {
            throw new JMokaException(glGetShaderInfoLog(program, 1024));
        }

        // Validate and check errors.
        glValidateProgram(program);

        if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0)
        {
            throw new JMokaException(glGetShaderInfoLog(program, 1024));
        }

        uniformLocations = new HashMap<>();

        JMokaLog.o(TAG, "Shader correctly created.");
    }

    public void update(final Transform transform, final Sprite sprite)
    {
        Matrix4 model = CoreUtils.getModelMatrix(transform);

        setUniform("u_model", model);
        setUniform("u_color", sprite.getTint());
    }

    public void bind()
    {
        glUseProgram(program);
    }

    private int createShader(String filePath, int type)
    {
        String code = Utils.readFile(filePath);

        int shader = glCreateShader(type);

        if (shader == 0)
        {
            throw new JMokaException("Shader creation failed for file " + filePath);
        }

        glShaderSource(shader, code);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0)
        {
            throw new JMokaException(
                    "Shader compile error for file " + filePath + ": " + glGetShaderInfoLog(shader, 1024));
        }

        glAttachShader(program, shader);
        return shader;
    }

    public int getUniformLocation(String uniform)
    {
        // if location is contained, return it.
        if (uniformLocations.containsKey(uniform))
        {
            return uniformLocations.get(uniform);
        }

        // if not, we ask openGL for the uniform location, store it and return it.
        int location = glGetUniformLocation(program, uniform);

        if (location == -1)
        {
            throw new JMokaException("No uniform with name " + uniform);
        }
        else
        {
            uniformLocations.put(uniform, location);
        }

        JMokaLog.o(TAG, "New uniform: " + uniform + ", at location " + location);
        return location;
    }

    /* setUniform's */
    public void setUniform(String uniform, Matrix4 matrix)
    {
        FloatBuffer buffer = Utils.genBuffer(matrix);
        glUniformMatrix4(getUniformLocation(uniform), false, buffer);
    }

    public void setUniform(String uniform, float x, float y, float z)
    {
        glUniform3f(getUniformLocation(uniform), x, y, z);
    }

    public void setUniform(String uniform, float v)
    {
        glUniform1f(getUniformLocation(uniform), v);
    }

    public void setUniform(String uniform, float x, float y)
    {
        glUniform2f(getUniformLocation(uniform), x, y);
    }

    public void setUniform(String uniform, Vector3f v)
    {
        setUniform(uniform, v.x, v.y, v.z);
    }

    public void setUniform(String uniform, Vector2f v)
    {
        setUniform(uniform, v.x, v.y);
    }

    private void setUniform(String uniform, Color color)
    {
        glUniform4f(getUniformLocation(uniform), color.r, color.g, color.b, color.a);
    }
}
