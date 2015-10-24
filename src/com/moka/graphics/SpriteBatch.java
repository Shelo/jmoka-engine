package com.moka.graphics;

import com.moka.components.Sprite;
import com.moka.math.Vector2;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class SpriteBatch
{
    private static final int COMPONENTS_PER_VERTEX = 8;
    private static final int MAX_SPRITES = 200;
    private static final int MAX_VERTICES = 4 * MAX_SPRITES;
    private static final int VERTEX_BUFFER_SIZE = COMPONENTS_PER_VERTEX * MAX_VERTICES;
    private static final int INDEX_BUFFER_SIZE = MAX_SPRITES * 6;

    private float[] vertices;
    private int[] indices;
    private int vbo;
    private int ibo;
    private int vao;

    private Texture texture;
    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;

    // current vertex buffer counter.
    private int vc;

    // current vertex index.
    private int vi;

    // current index buffer counter.
    private int ic;

    public SpriteBatch()
    {
        vertices = new float[VERTEX_BUFFER_SIZE];
        indices = new int[INDEX_BUFFER_SIZE];

        vertexBuffer = BufferUtils.createFloatBuffer(VERTEX_BUFFER_SIZE);
        indexBuffer = BufferUtils.createIntBuffer(INDEX_BUFFER_SIZE);

        vao = glGenVertexArrays();

        vbo = glGenBuffers();
        ibo = glGenBuffers();
    }

    public void draw(Sprite sprite)
    {
        Vector2 pos = sprite.getTransform().getPosition();
        draw(sprite.getTexture(), pos.x, pos.y, (int) sprite.getWidth(), (int) sprite.getHeight(), sprite.getTint());
    }

    public void draw(Texture texture, float x, float y, Color color)
    {
        draw(texture, x, y, texture.getWidth(), texture.getHeight(), color);
    }

    public void draw(Texture texture, float x, float y)
    {
        draw(texture, x, y, texture.getWidth(), texture.getHeight(), Color.WHITE);
    }

    public void draw(Texture texture, float x, float y, int width, int height, Color color)
    {
        // set the texture.
        setTexture(texture);

        // too large batch, render all.
        if (vc >= VERTEX_BUFFER_SIZE)
            render();

        // put every vertex in the buffer.
        vertices[vc++] = x;
        vertices[vc++] = y;
        vertices[vc++] = 0;
        vertices[vc++] = 0;
        vertices[vc++] = color.r;
        vertices[vc++] = color.g;
        vertices[vc++] = color.b;
        vertices[vc++] = color.a;

        vertices[vc++] = x;
        vertices[vc++] = y + height;
        vertices[vc++] = 0;
        vertices[vc++] = 1;
        vertices[vc++] = color.r;
        vertices[vc++] = color.g;
        vertices[vc++] = color.b;
        vertices[vc++] = color.a;

        vertices[vc++] = x + width;
        vertices[vc++] = y + height;
        vertices[vc++] = 1;
        vertices[vc++] = 1;
        vertices[vc++] = color.r;
        vertices[vc++] = color.g;
        vertices[vc++] = color.b;
        vertices[vc++] = color.a;

        vertices[vc++] = x + width;
        vertices[vc++] = y;
        vertices[vc++] = 1;
        vertices[vc++] = 0;
        vertices[vc++] = color.r;
        vertices[vc++] = color.g;
        vertices[vc++] = color.b;
        vertices[vc++] = color.a;

        indices[ic++] = vi;
        indices[ic++] = vi + 1;
        indices[ic++] = vi + 2;
        indices[ic++] = vi;
        indices[ic++] = vi + 2;
        indices[ic++] = vi + 3;

        vi += 4;
    }

    private void setTexture(Texture texture)
    {
        if (this.texture != null && texture != this.texture)
            render();

        this.texture = texture;
    }

    private void clean()
    {
        texture = null;
        vc = ic = vi = 0;
    }

    public void render()
    {
        vertexBuffer.clear();
        vertexBuffer.put(vertices);
        vertexBuffer.flip();

        indexBuffer.clear();
        indexBuffer.put(indices);
        indexBuffer.flip();

        texture.bind();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 8 * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 8 * 4, 2 * 4);
        glVertexAttribPointer(2, 4, GL_FLOAT, false, 8 * 4, 4 * 4);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        glDrawElements(GL_TRIANGLES, ic, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);

        clean();
    }

    public void debug()
    {
        System.out.println("Current vertex counter: " + vc);
        System.out.println("Current index counter: " + ic);
    }
}
