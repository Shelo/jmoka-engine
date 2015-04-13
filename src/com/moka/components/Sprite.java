package com.moka.components;

import com.moka.core.Component;
import com.moka.core.xml.XmlAttribute;
import com.moka.graphics.Color;
import com.moka.graphics.Quad;
import com.moka.graphics.Shader;
import com.moka.graphics.Texture;
import com.moka.math.Vector2f;
import com.moka.utils.JMokaException;

/**
 * Sprite class, this draws a Texture on a Quad given the transform specifications.
 * @author Shelo
 */
public class Sprite extends Component
{
	private Vector2f clipBottomLeft = null;
	private Vector2f clipTopRight = null;

	private Texture texture;
	private Vector2f size;
	private Color tint;
	private Quad quad;

	public Sprite()
	{
		tint = new Color(1, 1, 1, 1);
	}

	public Sprite(Texture texture, Vector2f size, Color tint)
	{
		this.texture = texture;
		this.tint = tint;
		this.size.set(size);
	}

	public Sprite(Texture texture, Color tint)
	{
		this(texture, null, tint);
	}

	public Sprite(Texture texture, Vector2f size)
	{
		this(texture, size, Color.WHITE);
	}

	public Sprite(Texture texture) {
		this(texture, null, Color.WHITE);
	}

	public Sprite(String filePath)
	{
		this(new Texture(filePath));
	}

	@Override
	public void onCreate()
	{
		if (clipTopRight != null)
		{
			clipTopRight.x = clipTopRight.x / texture.getWidth()  * texture.getTexCoordX();
			clipTopRight.y = clipTopRight.y / texture.getHeight() * texture.getTexCoordY();
		}
		else
		{
			clipTopRight = new Vector2f();

			clipTopRight.x = texture.getTexCoordX();
			clipTopRight.y = texture.getTexCoordY();
		}

		quad = new Quad(clipBottomLeft, clipTopRight);
	}

	public void render(Shader shader)
	{
		if(texture == null)
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

	public void setTexture(Texture texture)
	{
		this.texture = texture;
	}

	@XmlAttribute(value = "texture", required = true)
	public void setTexture(String path)
	{
		setTexture(Texture.newTexture(getResources(), path));
	}

	@XmlAttribute("tintR")
	public void setTintR(float value)
	{
		tint.r = value;
	}

	@XmlAttribute("tintG")
	public void setTintG(float value)
	{
		tint.g = value;
	}

	@XmlAttribute("tintB")
	public void setTintB(float value)
	{
		tint.b = value;
	}

	@XmlAttribute("tintA")
	public void setTintA(float value)
	{
		tint.a = value;
	}

	@XmlAttribute("width")
	public void setWidth(float value)
	{
		size.x = value;
	}

	@XmlAttribute("height")
	public void setHeight(float value)
	{
		size.y = value;
	}

	@XmlAttribute("clipTop")
	public void setClipTop(float clipTop)
	{
		if (clipTopRight == null)
		{
			clipTopRight = new Vector2f();
		}

		this.clipTopRight.y = clipTop;
	}

	@XmlAttribute("clipRight")
	public void setClipRight(float clipRight)
	{
		if (clipTopRight == null)
		{
			clipTopRight = new Vector2f();
		}

		this.clipTopRight.x = clipRight;
	}

	public Vector2f getSize()
	{
		if(size == null)
		{
			size = new Vector2f(texture.getWidth(), texture.getHeight());
		}

		return size;
	}

	public Quad getMesh()
	{
		return quad;
	}
}
