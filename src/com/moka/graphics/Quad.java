package com.moka.graphics;

import com.moka.core.Vertex;
import com.moka.math.Vector2f;
import com.moka.utils.CoreUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Quad
{
    private static final int VERTICES_COUNT = 4;
    private static final int INDICES_COUNT = 6;

    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;
    private Vertex[] vertices;
    private int vbo;
    private int vao;
    private int ibo;

    public Quad(float xTexCoord, float yTexCoord)
    {
        // create and bind the buffer.
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // create sub buffers.
        vbo = glGenBuffers();
        ibo = glGenBuffers();

        // generate vertices.
        // texture is drawn flipped on porpoise.
        vertices = new Vertex[]{
                new Vertex(-0.5f, -0.5f, 0, yTexCoord),
                new Vertex(-0.5f, 0.5f, 0, 0),
                new Vertex(0.5f, 0.5f, xTexCoord, 0),
                new Vertex(0.5f, -0.5f, xTexCoord, yTexCoord)
        };

        // populate vertexBuffer.
        vertexBuffer = CoreUtils.genBuffer(vertices);

        // populate indexBuffer.
        indexBuffer = CoreUtils.genBuffer(new int[]{
                0, 1, 2, 0, 2, 3
        });

        // Vertex Buffer Object.
        // positions and texCoords buffer.
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // populate VertexAttribArrays 0 (position) and 1 (texCoords).
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, Vertex.SIZE * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 2 * 4);

        // Vertex Index Buffer.
        // indices buffer.
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        // unbind Vertex Array.
        glBindVertexArray(0);
    }

    public void draw()
    {
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, INDICES_COUNT, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void dispose()
    {
        glDeleteVertexArrays(vao);
    }

    public Vertex[] getVertices()
    {
        return vertices;
    }

    public Vector2f[] getVerticesAsVector2()
    {
        // TODO: optimize vertices because they are all the same every time.
        Vector2f[] res = new Vector2f[vertices.length];

        for (int i = 0; i < res.length; i++)
        {
            res[i] = vertices[i].getXY();
        }

        return res;
    }
}
