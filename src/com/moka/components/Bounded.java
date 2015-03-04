package com.moka.components;

import com.moka.core.Transform;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector3;

public class Bounded extends Component {
	private float bottom	= 0;
	private float right		= 0;
	private float left		= 0;
	private float top		= 0;

	private Transform transform;
	private Sprite sprite;

	@Override
	public void onCreate() {
		transform = getTransform();
		sprite = getEntity().getComponent(Sprite.class);
	}

	@Override
	public void onUpdate(double delta) {
		Vector3 pos = transform.getPosition();

		if(pos.getX() < left + sprite.getWidth() / 2)
			pos.setX(left + sprite.getWidth() / 2);

		if(pos.getX() > right - sprite.getWidth() / 2)
			pos.setX(right - sprite.getWidth() / 2);

		if(pos.getY() < bottom + sprite.getHeight() / 2)
			pos.setY(bottom + sprite.getHeight() / 2);

		if(pos.getY() > top - sprite.getHeight() / 2)
			pos.setY(top - sprite.getHeight() / 2);
	}

	@XmlAttribute(value = "left", required = true)
	public void setLeft(float left) {
		this.left = left;
	}

	@XmlAttribute(value = "right", required = true)
	public void setRight(float right) {
		this.right = right;
	}

	@XmlAttribute(value = "top", required = true)
	public void setTop(float top) {
		this.top = top;
	}

	@XmlAttribute(value = "bottom", required = true)
	public void setBottom(float bottom) {
		this.bottom = bottom;
	}

}
