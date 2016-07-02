package com.moka.graphics;

import com.moka.components.Sprite;
import com.moka.core.Moka;
import com.moka.math.Matrix3;
import com.moka.math.Vector2;
import com.moka.utils.CoreUtil;
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
    private static final int MAX_SPRITES = 100;
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

    // debug.
    private int renderCounter;

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

        draw(sprite.getTexture(), pos.x, pos.y, (int) sprite.getWidth(), (int) sprite.getHeight(),
                sprite.getTint(), sprite.getTransform().getRotation());
    }

    public void draw(Texture texture, float x, float y, Color color)
    {
        draw(texture, x, y, texture.getWidth(), texture.getHeight(), color);
    }

    public void draw(Texture texture, float x, float y)
    {
        draw(texture, x, y, texture.getWidth(), texture.getHeight(), Color.WHITE);
    }

    public void draw(Texture texture, float x, float y, int width, int height, Color color,
                     Matrix3 rotation)
    {
        // set the texture.
        setTexture(texture);

        // too large batch, render all.
        if (vc >= VERTEX_BUFFER_SIZE) {
            render();
        }

        float minX = - width / 2;
        float minY = - height / 2;
        float maxX = width / 2;
        float maxY = height / 2;

        // put every vertex in the buffer.
        // bottom left.
        vertices[vc++] = minX * rotation.get(0, 0) + minY * rotation.get(0, 1) + x;
        vertices[vc++] = minX * rotation.get(1, 0) + minY * rotation.get(1, 1) + y;
        vertices[vc++] = 0;
        vertices[vc++] = 0;
        vertices[vc++] = color.r;
        vertices[vc++] = color.g;
        vertices[vc++] = color.b;
        vertices[vc++] = color.a;

        // top left.
        vertices[vc++] = minX * rotation.get(0, 0) + maxY * rotation.get(0, 1) + x;
        vertices[vc++] = minX * rotation.get(1, 0) + maxY * rotation.get(1, 1) + y;
        vertices[vc++] = 0;
        vertices[vc++] = 1;
        vertices[vc++] = color.r;
        vertices[vc++] = color.g;
        vertices[vc++] = color.b;
        vertices[vc++] = color.a;

        // top right.
        vertices[vc++] = maxX * rotation.get(0, 0) + maxY * rotation.get(0, 1) + x;
        vertices[vc++] = maxX * rotation.get(1, 0) + maxY * rotation.get(1, 1) + y;
        vertices[vc++] = 1;
        vertices[vc++] = 1;
        vertices[vc++] = color.r;
        vertices[vc++] = color.g;
        vertices[vc++] = color.b;
        vertices[vc++] = color.a;

        // bottom right.
        vertices[vc++] = maxX * rotation.get(0, 0) + minY * rotation.get(0, 1) + x;
        vertices[vc++] = maxX * rotation.get(1, 0) + minY * rotation.get(1, 1) + y;
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

    public void draw(Texture texture, float x, float y, int width, int height, Color color)
    {
        // Code duplicated to avoid non useful multiplication.

        // set the texture.
        setTexture(texture);

        // batch overflow, render all.
        if (vc >= VERTEX_BUFFER_SIZE) {
            render();
        }

        float minX = - width / 2;
        float minY = - height / 2;
        float maxX = width / 2;
        float maxY = height / 2;

        // put every vertex in the buffer.
        // bottom left.
        vertices[vc++] = minX + x;
        vertices[vc++] = minY + y;
        vertices[vc++] = 0;
        vertices[vc++] = 0;
        vertices[vc++] = color.r;
        vertices[vc++] = color.g;
        vertices[vc++] = color.b;
        vertices[vc++] = color.a;

        // top left.
        vertices[vc++] = minX + x;
        vertices[vc++] = maxY + y;
        vertices[vc++] = 0;
        vertices[vc++] = 1;
        vertices[vc++] = color.r;
        vertices[vc++] = color.g;
        vertices[vc++] = color.b;
        vertices[vc++] = color.a;

        // top right.
        vertices[vc++] = maxX + x;
        vertices[vc++] = maxY + y;
        vertices[vc++] = 1;
        vertices[vc++] = 1;
        vertices[vc++] = color.r;
        vertices[vc++] = color.g;
        vertices[vc++] = color.b;
        vertices[vc++] = color.a;

        // bottom right.
        vertices[vc++] = maxX + x;
        vertices[vc++] = minY + y;
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
        if (this.texture != null && texture != this.texture) {
            render();
        }

        this.texture = texture;
    }

    private void clean()
    {
        texture = null;
        vc = ic = vi = 0;
    }

    public void render()
    {
        // check if there's something to draw.
        if (vc == 0) {
            return;
        }

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

        renderCounter++;

        clean();
    }

    public int getSpritesInBatch()
    {
        return (int) (vc / COMPONENTS_PER_VERTEX * 0.25f);
    }

    public Texture getCurrentTexture()
    {
        return texture;
    }
}
