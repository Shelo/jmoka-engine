package com.moka.components;

import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.graphics.Color;
import com.moka.graphics.Quad;
import com.moka.graphics.Shader;
import com.moka.graphics.Texture;
import com.moka.math.Vector2;
import com.moka.math.Rectangle;
import com.moka.utils.JMokaException;

import static org.lwjgl.opengl.GL11.*;

/**
 * Sprite class, this draws a Texture on a Quad given the transform specifications.
 *
 * @author Shelo
 */
public class Sprite extends Component
{
    private Rectangle clipRect;
    private Texture texture;
    private Vector2 size;
    private Color tint;
    private Quad quad;
    private BLEND blend = BLEND.NORMAL;

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
        {
            this.size.set(size);
        }
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
        if (clipRect == null)
        {
            clipRect = new Rectangle(0, 0, 1, 1);
        }

        quad = new Quad(clipRect);
    }

    public void render(Shader shader)
    {
        if (texture == null)
            raise("there's no texture to draw.");

        // render.
        texture.bind();
        shader.update(getTransform(), this);

        switch (blend)
        {
            case NORMAL:
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                break;
            case ADDITIVE:
                glBlendFunc(GL_SRC_ALPHA, GL_ONE);
                break;
        }

        quad.draw();
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

    @ComponentAttribute(value = "Texture", required = true)
    public void setTexture(Texture texture)
    {
        if (texture == null)
        {
            throw new JMokaException("Texture cannot be null. [" + getEntity().getName() + "]");
        }

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

    public Vector2 getSize()
    {
        if (size == null)
        {
            size = new Vector2(texture.getWidth(), texture.getHeight());
        }

        return size;
    }

    public Quad getMesh()
    {
        return quad;
    }
}
