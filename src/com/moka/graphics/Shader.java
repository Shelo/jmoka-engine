package com.moka.graphics;

import com.moka.components.Sprite;
import com.moka.core.Moka;
import com.moka.scene.entity.Transform;
import com.moka.math.Matrix3;
import com.moka.math.Matrix4;
import com.moka.math.Vector2;
import com.moka.math.Vector3;
import com.moka.utils.CalcUtil;
import com.moka.utils.CoreUtil;
import com.moka.utils.JMokaException;
import com.moka.utils.JMokaLog;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Shader
{
    public static final String TAG = "SHADER";

    private HashMap<String, Integer> uniformLocations;
    private boolean bound;
    private int program;

    public Shader(String vertexCode, String fragmentCode)
    {
        program = glCreateProgram();
        createShader(vertexCode, GL_VERTEX_SHADER);
        createShader(fragmentCode, GL_FRAGMENT_SHADER);

        // Link and check errors.
        glLinkProgram(program);

        glBindAttribLocation(program, 0, "a_position");
        glBindAttribLocation(program, 1, "a_texCoord");

        if (glGetProgrami(program, GL_LINK_STATUS) == 0)
        {
            throw new JMokaException(glGetShaderInfoLog(program, 1024));
        }

        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Validate and check errors.
        glValidateProgram(program);

        if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0)
            throw new JMokaException(glGetProgramInfoLog(program, 1024));

        uniformLocations = new HashMap<>();

        JMokaLog.o(TAG, "Shader correctly created.");
    }

    public void update(final Transform transform, final Sprite sprite)
    {
        Matrix3 model = CalcUtil.calcModelMatrix(transform);

        setUniform("u_model", model);
        setUniform("u_color", sprite.getTint());
    }

    public void bind()
    {
        glUseProgram(program);
        Moka.getRenderer().setBound(this);
    }

    private int createShader(String code, int type)
    {
        int shader = glCreateShader(type);

        if (shader == 0)
            throw new JMokaException("Shader creation failed.");

        glShaderSource(shader, code);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0)
            throw new JMokaException("Shader compile error: " + glGetShaderInfoLog(shader, 1024));

        glAttachShader(program, shader);

        return shader;
    }

    public int getUniformLocation(String uniform)
    {
        // if location is contained, return it.
        if (uniformLocations.containsKey(uniform))
            return uniformLocations.get(uniform);

        // if not, we ask openGL for the uniform location, store it and return it.
        int location = glGetUniformLocation(program, uniform);

        if (location == -1)
            throw new JMokaException("No uniform with name " + uniform);
        else
            uniformLocations.put(uniform, location);

        JMokaLog.o(TAG, "New uniform: " + uniform + ", at location " + location);
        return location;
    }

    /* setUniform's */
    public void setUniform(String uniform, Matrix4 matrix)
    {
        FloatBuffer buffer = CoreUtil.genBuffer(matrix);
        glUniformMatrix4fv(getUniformLocation(uniform), false, buffer);
    }

    public void setUniform(String uniform, Matrix3 matrix)
    {
        FloatBuffer buffer = CoreUtil.genBuffer(matrix);
        glUniformMatrix3fv(getUniformLocation(uniform), false, buffer);
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

    public void setUniform(String uniform, Vector3 v)
    {
        setUniform(uniform, v.x, v.y, v.z);
    }

    public void setUniform(String uniform, Vector2 v)
    {
        setUniform(uniform, v.x, v.y);
    }

    private void setUniform(String uniform, Color color)
    {
        glUniform4f(getUniformLocation(uniform), color.r, color.g, color.b, color.a);
    }

    public boolean isBound()
    {
        return Moka.getRenderer().isBound(this);
    }
}
