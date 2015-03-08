package com.moka.core;

import com.moka.exceptions.JMokaException;
import com.moka.math.Matrix4;
import com.moka.math.Quaternion;
import com.moka.math.Vector2;
import com.moka.math.Vector3;

public final class Transform {
	private final Entity entity;

	private Vector3 prevPosition;
	private Vector3 position;

	private Quaternion prevRotation;
	private Quaternion rotation;

	// TODO: this.
	private Vector2 prevSize;
	private Vector2 size;

	public Transform(Entity entity) {
		this.entity = entity;
		rotation = Quaternion.IDENTITY.copy();
		position = Vector3.ZERO.copy();
	}

	public void update() {
		prevPosition = position.copy();
		prevRotation = rotation.copy();
	}

	public Matrix4 getModelMatrix() {
		Matrix4 translation = Matrix4.translate((int) position.x, (int) position.y,
				(int) position.z);
		Matrix4 scale = Matrix4.scale(getSize().x, getSize().y, 1);
		Matrix4 rotate = rotation.toRotationMatrix();
		return translation.mul(rotate.mul(scale));
	}

	public void move(float x, float y) {
		position = position.add(x, y, 0.0f);
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

	public void setRotRadians(float rotation) {
		this.rotation = new Quaternion(Vector3.AXIS_Z, rotation);
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
		position.set(x, y, position.z);
	}

	public void setPosition(Vector3 position) {
		this.position.set(position);
	}

	public void setPosition(Vector2 position) {
		this.position.set(position);
	}

	public float getPositionX() {
		return position.x;
	}

	public float getPositionY() {
		return position.y;
	}

	public float getPositionZ() {
		return 0;
	}

	public boolean hasRotated() {
		return prevRotation == null || !prevRotation.equals(rotation);

	}

	public boolean hasMoved() {
		return prevPosition == null || !prevPosition.equals(position);
	}

	public boolean hasChanged() {
		return hasRotated() || hasMoved();
	}

	public Vector2 getPosition() {
		return position.getXY();
	}

	public void move(Vector2 movement) {
		move(movement.x, movement.y);
	}

	public int getLayer() {
		return (int) position.z;
	}
}
