package com.moka.components;

import com.moka.core.xml.XmlAttribute;
import com.moka.physics.Collider;
import com.moka.physics.Collision;

public class CircleCollider extends Collider {
	private float radius;

	@Override
	public void onCreate() {
		if(radius == 0) {
			float width  = getEntity().getSprite().getWidth();
			float height = getEntity().getSprite().getHeight();
			radius = (width + height) / 4;
		}
	}

	@Override
	public Collision collidesWith(Collider other) {
		if(other instanceof CircleCollider) {
			return circle(this, (CircleCollider) other);
		}

		return null;
	}

	@Override
	public void response(Collision collision) {
		getTransform().move(collision.getMovement());
	}

	@XmlAttribute("radius")
	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}
}
