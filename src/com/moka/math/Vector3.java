package com.moka.math;

public class Vector3 {
	public static final Vector3 AXIS_Y 	= new Vector3(0, 1, 0);
	public static final Vector3 AXIS_X 	= new Vector3(1, 0, 0);
	public static final Vector3 AXIS_Z 	= new Vector3(0, 0, 1);
	public static final Vector3 ZERO 	= new Vector3(0, 0, 0);
	public static final Vector3 ONE 	= new Vector3(1, 1, 1);

	private float x;
	private float y;
	private float z;

	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3() {
		this(0, 0, 0);
	}

	public Vector3(Vector3 other) {
		set(other);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public float max() {
		return Math.max(x, Math.max(y, z));
	}

	public float dot(Vector3 r) {
		return x * r.x + y * r.y + z * r.z;
	}

	public Vector3 cross(Vector3 r) {
		float x_ = y * r.z - z * r.y;
		float y_ = z * r.x - x * r.z;
		float z_ = x * r.y - y * r.x;

		return new Vector3(x_, y_, z_);
	}

	public Vector3 normalize() {
		float length = length();
		this.x /= length;
		this.y /= length;
		this.z /= length;

		return this;
	}

	public Vector3 normalized() {
		float length = length();

		return new Vector3(x / length, y / length, z / length);
	}

	public Vector3 rotate(Vector3 axis, float radians) {
		Quaternion rotation = new Quaternion(axis, radians);
		Quaternion conjugate = rotation.conjugate();
		Quaternion w = rotation.mul(this).mul(conjugate);
		return set(w.getX(), w.getY(), w.getZ());
	}

	public Vector3 rotate(Quaternion rotation) {
		Quaternion conjugate = rotation.conjugate();
		Quaternion w = rotation.mul(this).mul(conjugate);
		return new Vector3(w.getX(), w.getY(), w.getZ());
	}

	public Vector3 lerp(Vector3 dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	public Vector3 add(Vector3 r) {
		return new Vector3(x + r.x, y + r.y, z + r.z);
	}

	public Vector3 add(float x, float y, float z) {
		return new Vector3(this.x + x, this.y + y, this.z + z);
	}

	public Vector3 add(float r) {
		return new Vector3(x + r, y + r, z + r);
	}

	public Vector3 sub(Vector3 r) {
		return new Vector3(x - r.x, y - r.y, z - r.z);
	}

	public Vector3 sub(float r) {
		return new Vector3(x - r, y - r, z - r);
	}

	public Vector3 mul(Vector3 r) {
		return new Vector3(x * r.x, y * r.y, z * r.z);
	}

	public Vector3 mul(float r) {
		return new Vector3(x * r, y * r, z * r);
	}

	public Vector3 div(Vector3 r) {
		return new Vector3(x / r.x, y / r.y, z / r.z);
	}

	public Vector3 div(float r) {
		return new Vector3(x / r, y / r, z / r);
	}

	public Vector3 abs() {
		return new Vector3(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	public Vector2 getXY() {
		return new Vector2(x, y);
	}

	public Vector2 getYZ() {
		return new Vector2(y, z);
	}

	public Vector2 getZX() {
		return new Vector2(z, x);
	}

	public Vector2 getYX() {
		return new Vector2(y, x);
	}

	public Vector2 getZY() {
		return new Vector2(z, y);
	}

	public Vector2 getXZ() {
		return new Vector2(x, z);
	}

	public Vector3 set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3 copy() {
		return new Vector3(x, y, z);
	}

	public Vector3 set(Vector3 r) {
		set(r.x, r.y, r.z);
		return this;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public String toString() {
		return "(" + x + " " + y + " " + z + ")";
	}
	
	public boolean equals(Vector3 r) {
		return x == r.x && y == r.y && z == r.z;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void set(Vector2 position) {
		x = position.getX();
		y = position.getY();
	}
}