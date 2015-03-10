package com.moka.components;

import com.moka.core.xml.XmlAttribute;
import com.moka.core.xml.XmlSupported;
import com.moka.exceptions.JMokaException;
import com.moka.graphics.Color;
import com.moka.graphics.Quad;
import com.moka.graphics.Shader;
import com.moka.graphics.Texture;
import com.moka.math.Vector2f;

/**
 * Sprite class, this draws a Texture on a Quad given the transform specifications.
 * @author Shelo
 */
@XmlSupported
public class Sprite extends Component {
	private Texture texture;
	private Vector2f size;
	private Color tint;
	private Quad quad;
	private float tintR = 1;
	private float tintG = 1;
	private float tintB = 1;
	private float tintA = 1;

	public Sprite() {
		tint = new Color(1, 1, 1, 1);
	}

	public Sprite(Texture texture, Vector2f size, Color tint) {
		this.texture 	= texture;
		this.tint 		= tint;

		this.size.set(size);
		quad = new Quad(texture.getTexCoordX(), texture.getTexCoordY());
	}

	public Sprite(Texture texture, Color tint) {
		this(texture, null, tint);
	}

	public Sprite(Texture texture, Vector2f size) {
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
			throw new JMokaException("Sprite: there no texture to draw.");

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
		return size == null ? texture.getWidth() : size.x;
	}

	public float getHeight() {
		return size == null ? texture.getHeight() : size.y;
	}

	public Color getTint() {
		return tint;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
		quad = new Quad(texture.getTexCoordX(), texture.getTexCoordY());
	}

	@XmlAttribute(value = "texture", required = true)
	public void setTexture(String path) {
		setTexture(Texture.newTexture(path));
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
		size.x = value;
	}

	@XmlAttribute("height")
	public void setHeight(float value) {
		size.y = value;
	}

	public Vector2f getSize() {
		if(size == null) {
			size = new Vector2f(texture.getWidth(), texture.getHeight());
		}
		return size;
	}

	public Quad getMesh() {
		return quad;
	}
}
