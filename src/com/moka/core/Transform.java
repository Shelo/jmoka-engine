package com.moka.core;

import com.moka.exceptions.JMokaException;
import com.moka.math.Matrix4;
import com.moka.math.Quaternion;
import com.moka.math.Vector2;
import com.moka.math.Vector3;

public final class Transform {
	private final Entity entity;
	
	private Quaternion rotation;
	private Vector3 position;
	private Vector2 size;

	private boolean rotated;

	public Transform(Entity entity) {
		this.entity = entity;

		position = new Vector3(Vector3.ZERO);
		rotation = new Quaternion(Quaternion.IDENTITY);
	}

	public Matrix4 getModelMatrix() {
		Matrix4 translation = Matrix4.translate(position.getX(), position.getY(), position.getZ());
		Matrix4 scale = Matrix4.scale(getSize().getX(), getSize().getY(), 1);
		Matrix4 rotate = rotation.toRotationMatrix();
		return translation.mul(rotate.mul(scale));
	}

	public void move(float x, float y, float z) {
		Vector3 _position = getPosition();
		float _x = _position.getX();
		float _y = _position.getY();
		float _z = _position.getZ();
		position = new Vector3(_x + x, _y + y, _z + z);
	}

	public void move(float x, float y) {
		move(x, y, 0);
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public Vector3 getPosition() {
		return position;
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
		position.set(x, y, position.getZ());
	}

	public void setPosition(Vector2 position) {
		this.position.set(position.getX(), position.getY(), this.position.getZ());
	}

	public void setPosition(Vector3 position) {
		this.position.set(position);
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
		rotated = true;
	}

	public void setRotation(float rotation) {
		this.rotation = new Quaternion(Vector3.AXIS_Z, (float) Math.toRadians(rotation));
		rotated = true;
	}

	public boolean hasRotated() {
		return rotated | (rotated = false);
	}

	public void setSize(int x, int y) {
		if(size == null)
			size = new Vector2(x, y);
		else
			size.set(x, y);
	}
}
