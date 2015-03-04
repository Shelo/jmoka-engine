package com.moka.core;

import com.moka.math.Matrix4;
import com.moka.math.Quaternion;
import com.moka.math.Vector2;
import com.moka.math.Vector3;

public final class Transform {
	private Quaternion rotation;
	private Vector3 position;
	private Vector2 scale;

	public Transform() {
		position 	= new Vector3(Vector3.ZERO);
		rotation	= new Quaternion(Quaternion.IDENTITY);
		scale 		= new Vector2(Vector2.ONE);
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

	public void setScale(Vector2 scale) {
		this.scale = scale;
	}

	public Matrix4 getModelMatrix(float xSize, float ySize) {
		Matrix4 translation = Matrix4.translate(position.getX(), position.getY(), position.getZ());
		Matrix4 scale 		= Matrix4.scale(xSize * this.scale.getX(), ySize * this.scale.getY(), 1);
		Matrix4 rotate 		= rotation.toRotationMatrix();

		return translation.mul(rotate.mul(scale));
	}

	public Vector2 getScale() {
		return scale;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public Vector3 getPosition() {
		return position;
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

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = new Quaternion(Vector3.AXIS_Z, (float) Math.toRadians(rotation));
	}
}
