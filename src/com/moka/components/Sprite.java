package com.moka.components;

import com.moka.core.xml.XmlAttribute;
import com.moka.core.xml.XmlSupported;
import com.moka.exceptions.JMokaException;
import com.moka.graphics.Color;
import com.moka.graphics.Quad;
import com.moka.graphics.Shader;
import com.moka.graphics.Texture;
import com.moka.math.Vector2;

/**
 * Sprite class, this draws a Texture on a Quad given the transform specifications.
 * @author Shelo
 */
@XmlSupported
public class Sprite extends Component {
	private Texture texture;
	private Color tint;
	private Quad quad;

	private float height = 0;
	private float width = 0;
	private float tintR = 1;
	private float tintG = 1;
	private float tintB = 1;
	private float tintA = 1;

	public Sprite() {
		tint = new Color(1, 1, 1, 1);
	}

	public Sprite(Texture texture, Vector2 size, Color tint) {
		this.texture 	= texture;
		this.tint 		= tint;

		width 	= size.getX();
		height 	= size.getY();

		quad = new Quad(texture.getXTexCoord(), texture.getYTexCoord());
	}

	public Sprite(Texture texture, Color tint) {
		this(texture, null, tint);
	}

	public Sprite(Texture texture, Vector2 size) {
		this(texture, size, Color.WHITE);
	}

	public Sprite(Texture texture) {
		this(texture, null, Color.WHITE);
	}

	public Sprite(String filePath) {
		this(new Texture(filePath));
	}

	public void render(Shader shader) {
		if(texture == null)
			JMokaException.raise("Sprite: there no texture to draw.");

		tint.set(tintR, tintG, tintB, tintA);

		// render.
		texture.bind();
		shader.update(getTransform(), this);
		quad.draw();
	}

	public Texture getTexture() {
		return texture;
	}

	public Quad getQuad() {
		return quad;
	}

	public float getWidth() {
		return width == 0 ? texture.getWidth() : width;
	}

	public float getHeight() {
		return height == 0 ? texture.getHeight() : height;
	}

	public Color getTint() {
		return tint;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
		quad = new Quad(texture.getXTexCoord(), texture.getYTexCoord());
	}

	@XmlAttribute(value = "texture", required = true)
	public void setTexture(String texture) {
		setTexture(new Texture(texture));
	}

	@XmlAttribute("tintR")
	public void setTintR(float value) {
		tintR = value;
	}

	@XmlAttribute("tintG")
	public void setTintG(float value) {
		tintG = value;
	}

	@XmlAttribute("tintB")
	public void setTintB(float value) {
		tintB = value;
	}

	@XmlAttribute("tintA")
	public void setTintA(float value) {
		tintA = value;
	}

	@XmlAttribute("width")
	public void setWidth(float value) {
		width = value;
	}

	@XmlAttribute("height")
	public void setSizeY(float value) {
		height = value;
	}
}
