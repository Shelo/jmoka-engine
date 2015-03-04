package com.moka.core;

import com.moka.exceptions.JMokaException;
import com.moka.math.Matrix4;
import com.moka.math.Quaternion;
import com.moka.math.Vector2;
import com.moka.math.Vector3;

public final class Transform {
	private final Entity entity;

	private float x = 0;
	private float y = 0;
	private float z = 0;

	private Quaternion prevRotation;
	private Quaternion rotation;
	private Vector2 size;

	public Transform(Entity entity) {
		this.entity = entity;
		rotation = new Quaternion(Quaternion.IDENTITY);
	}

	public Matrix4 getModelMatrix() {
		Matrix4 translation = Matrix4.translate((int) x, (int) y, (int) z);
		Matrix4 scale = Matrix4.scale(getSize().getX(), getSize().getY(), 1);
		Matrix4 rotate = rotation.toRotationMatrix();
		return translation.mul(rotate.mul(scale));
	}

	public void move(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
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

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setPosition(Vector2 position) {
		x = position.getX();
		y = position.getY();
	}

	public void setPosition(Vector3 position) {
		x = position.getX();
		y = position.getY();
		z = position.getZ();
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public void setRotation(Quaternion rotation) {
		prevRotation = this.rotation.copy();
		this.rotation = rotation;
	}

	public void setRotation(float rotation) {
		prevRotation = this.rotation.copy();
		this.rotation = new Quaternion(Vector3.AXIS_Z, (float) Math.toRadians(rotation));
	}

	public boolean hasRotated() {
		return !prevRotation.equals(rotation);
	}

	public void setSize(int x, int y) {
		if(size == null)
			size = new Vector2(x, y);
		else
			size.set(x, y);
	}
}
