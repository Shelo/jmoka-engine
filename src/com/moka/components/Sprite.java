package com.moka.components;

import com.moka.graphics.*;
import com.moka.math.Rectangle;
import com.moka.math.Vector2;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.utils.JMokaException;

import static org.lwjgl.opengl.GL11.*;

/**
 * Sprite class, this draws a Texture on a Quad given the transform specifications.
 *
 * @author Shelo
 */
public class Sprite extends DrawableComponent
{
    private Rectangle clipRect;
    private Texture texture;
    private Vector2 size;
    private Color tint;
    private Quad quad;
    private BLEND blend = BLEND.NORMAL;
    private boolean batch = false;

    public enum BLEND
    {
        NORMAL,
        ADDITIVE,
    }

    public Sprite()
    {
        tint = new Color(1, 1, 1, 1);
    }

    public Sprite(Texture texture, Vector2 size, Color tint)
    {
        this.texture = texture;
        this.tint = tint;

        if (size != null)
            this.size.set(size);
    }

    public Sprite(Texture texture, Color tint)
    {
        this(texture, null, tint);
    }

    public Sprite(Texture texture, Vector2 size)
    {
        this(texture, size, Color.WHITE);
    }

    public Sprite(Texture texture)
    {
        this(texture, Color.WHITE);
    }

    public Sprite(String filePath)
    {
        this(new Texture(filePath));
    }

    @Override
    public void onCreate()
    {
        if (clipRect == null) {
            clipRect = new Rectangle(0, 0, 1, 1);
        }

        quad = new Quad(clipRect);
    }

    @Override
    public void render(Renderer renderer)
    {
        if (texture == null) {
            raiseError("there's no texture to draw.");
        }

        // if batch option is enabled, just batch.
        if (batch) {
            renderBatch(renderer);
            return;
        }

        texture.bind();
        renderer.getShader().update(getTransform(), this);

        switch (blend) {
            case NORMAL:
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                break;
            case ADDITIVE:
                glBlendFunc(GL_SRC_ALPHA, GL_ONE);
                break;
        }

        quad.draw();
    }

    @Override
    public boolean shouldBatch()
    {
        return batch;
    }

    public void renderBatch(Renderer renderer)
    {
        Vector2 position = getTransform().getPosition();
        Vector2 size = getTransform().getSize();

        renderer.batch(texture, position.x, position.y, (int) size.x, (int) size.y, tint,
                getTransform().getRotation());
    }

    public Texture getTexture()
    {
        return texture;
    }

    public Quad getQuad()
    {
        return quad;
    }

    public float getWidth()
    {
        return size == null ? texture.getWidth() : size.x;
    }

    public float getHeight()
    {
        return size == null ? texture.getHeight() : size.y;
    }

    public Color getTint()
    {
        return tint;
    }

    @Override
    public void onDestroy()
    {
        quad.dispose();
    }

    @ComponentAttribute(value = "Texture", required = true)
    public void setTexture(Texture texture)
    {
        if (texture == null)
            throw new JMokaException("Texture cannot be null. [" + getEntity().getName() + "]");

        this.texture = texture;
    }

    @ComponentAttribute("ClipRectPixels")
    public void setClipRectPixels(float left, float top, float width, float height)
    {
        this.clipRect = new Rectangle(left / texture.getWidth(), top / texture.getHeight(),
                width / texture.getWidth(), height / texture.getHeight());
    }

    @ComponentAttribute("ClipRect")
    public void setClipRect(float left, float top, float width, float height)
    {
        this.clipRect = new Rectangle(left, top, width, height);
    }

    @ComponentAttribute("Tint")
    public void setTint(float r, float g, float b, float a)
    {
        tint.set(r, g, b, a);
    }

    @ComponentAttribute("Blend")
    public void setBlend(BLEND blend)
    {
        this.blend = blend;
    }

    @ComponentAttribute("Batch")
    public void setBatch(boolean batch)
    {
        this.batch = batch;
    }

    public Vector2 getSize()
    {
        if (size == null)
            size = new Vector2(texture.getWidth(), texture.getHeight());

        return size;
    }

    public Quad getMesh()
    {
        return quad;
    }
}
