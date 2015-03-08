package com.moka.components;

import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2;
import com.moka.physics.Collider;
import com.moka.physics.Collision;

public class AABBCollider extends Collider {
	private float offx;
	private float offy;
	private float width;
	private float height;

	private boolean shouldInitWidth = true;
	private boolean shouldInitHeight = true;

	private CircleCollider boundingCircle;

	@Override
	public void onCreate() {
		if (shouldInitWidth)
			width = getTransform().getSize().x;

		if (shouldInitHeight)
			height = getTransform().getSize().y;
	}

	@XmlAttribute("offsetX")
	public void setOffsetX(float offx) {
		this.offx = offx;
	}

	@XmlAttribute("offsetY")
	public void setOffsetY(float offy) {
		this.offy = offy;
	}

	@XmlAttribute("width")
	public void setWith(float width) {
		shouldInitWidth = false;
		this.width = width;
	}

	@XmlAttribute("height")
	public void setHeight(float height) {
		shouldInitHeight = false;
		this.height = height;
	}

	public float getTop() {
		return getTransform().getPositionY() + height / 2;
	}

	public float getBot() {
		return getTransform().getPositionY() + offy - height / 2;
	}

	public float getLeft() {
		return getTransform().getPositionX() + offx - width / 2;
	}

	public float getRight() {
		return getTransform().getPositionX() + width / 2;
	}

	@Override
	public Collision collidesWith(Collider other) {
		if (other instanceof AABBCollider)
			return Collider.aabb(this, (AABBCollider) other);

		else if (other instanceof CircleCollider)
			return Collider.aabbCircle(this, (CircleCollider) other);

		return null;
	}

	@Override
	public void response(Collision collision) {
		Vector2 newPos = getTransform().getPosition().add(collision.getMovement());
		getTransform().setPosition(newPos);
	}

	public CircleCollider getBoundingCircle() {
		if (boundingCircle == null) {
			boundingCircle = new CircleCollider();
			boundingCircle.setEntity(getEntity());
			boundingCircle.setRadius((float) Math.sqrt(width * width + height * height) / 2);
		}

		return boundingCircle;
	}
}
