package com.moka.components;

import com.moka.core.entity.Component;
import com.moka.core.ComponentAttribute;
import com.moka.graphics.Color;
import com.moka.graphics.Quad;
import com.moka.graphics.Shader;
import com.moka.graphics.Texture;
import com.moka.math.Vector2;
import com.moka.utils.JMokaException;

/**
 * Sprite class, this draws a Texture on a Quad given the transform specifications.
 *
 * @author Shelo
 */
public class Sprite extends Component
{

    private Texture texture;
    private Vector2 size;
    private Color tint;
    private Quad quad;

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
        Vector2 clipTopRight = new Vector2();

        clipTopRight.x = texture.getTexCoordX();
        clipTopRight.y = texture.getTexCoordY();

        quad = new Quad(null, clipTopRight);
    }

    public void render(Shader shader)
    {
        if (texture == null)
        {
            throw new JMokaException("Sprite: there no texture to draw.");
        }

        // render.
        texture.bind();
        shader.update(getTransform(), this);
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

    @ComponentAttribute("Texture")
    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    public void setTexture(String path)
    {
        setTexture(new Texture(path));
    }

    @ComponentAttribute("TintR")
    public void setTintR(float value)
    {
        tint.r = value;
    }

    @ComponentAttribute("TintG")
    public void setTintG(float value)
    {
        tint.g = value;
    }

    @ComponentAttribute("TintB")
    public void setTintB(float value)
    {
        tint.b = value;
    }

    @ComponentAttribute("TintA")
    public void setTintA(float value)
    {
        tint.a = value;
    }

    @ComponentAttribute("Width")
    public void setWidth(float value)
    {
        if (size == null)
        {
            size = new Vector2();
        }

        size.x = value;
    }

    @ComponentAttribute("Height")
    public void setHeight(float value)
    {
        if (size == null)
        {
            size = new Vector2();
        }

        size.y = value;
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

    public void setTint(float r, float g, float b, float a)
    {
        tint.set(r, g, b, a);
    }
}
