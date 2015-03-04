package com.moka.core;

import com.moka.exceptions.JMokaException;
import com.moka.math.Matrix4;
import com.moka.math.Quaternion;
import com.moka.math.Vector2;
import com.moka.math.Vector3;

public final class Transform {
	private final Entity entity;

	private Vector2 prevPosition;
	private Vector2 position;

	private Quaternion prevRotation;
	private Quaternion rotation;

	// TODO: this.
	private Vector2 prevSize;
	private Vector2 size;

	public Transform(Entity entity) {
		this.entity = entity;
		rotation = Quaternion.IDENTITY.copy();
		position = Vector2.ZERO.copy();
	}

	public void update() {
		prevPosition = position.copy();
		prevRotation = rotation.copy();
	}

	public Matrix4 getModelMatrix() {
		Matrix4 translation = Matrix4.translate((int) position.getX(), (int) position.getY(), (int) 0);
		Matrix4 scale = Matrix4.scale(getSize().getX(), getSize().getY(), 1);
		Matrix4 rotate = rotation.toRotationMatrix();
		return translation.mul(rotate.mul(scale));
	}

	public void move(float x, float y, float z) {
		position = position.add(x, y);
	}

	public void move(float x, float y) {
		move(x, y, 0);
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public Vector2 getSize() {
		Vector2 rSize;

		if(size == null) {
			if(entity.hasSprite()) {
				rSize = entity.getSprite().getSize();

				if(rSize == null) {
					throw new JMokaException("Entity has no dimensions!");
				}

			} else {
				throw new JMokaException("Entity has no dimensions!");
			}
		} else {
			rSize = size;
		}

		return rSize;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = new Quaternion(Vector3.AXIS_Z, (float) Math.toRadians(rotation));
	}

	public void setSize(int x, int y) {
		if(size == null)
			size = new Vector2(x, y);
		else
			size.set(x, y);
	}

	public void setPosition(float x, float y) {
		position.set(x, y);
	}

	public void setPosition(Vector2 position) {
		this.position.set(position);
	}

	public void setPosition(Vector3 position) {
		this.position.set(position.getX(), position.getY());
	}

	public float getPositionX() {
		return position.getX();
	}

	public float getPositionY() {
		return position.getY();
	}

	public float getPositionZ() {
		return 0;
	}

	public boolean hasRotated() {
		return !prevRotation.equals(rotation);
	}

	public boolean hasMoved() {
		return !prevPosition.equals(position);
	}

	public boolean hasChanged() {
		return hasRotated() || hasMoved();
	}

	public Vector2 getPosition() {
		return position;
	}

	public void move(Vector2 movement) {
		move(movement.getX(), movement.getY());
	}
}
