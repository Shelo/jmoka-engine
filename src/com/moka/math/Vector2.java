package com.moka.math;

/**
 * Vector2 object representation of a 2D Vector.
 * This class can only change its values by using the method set(x, y).
 */
public class Vector2 {
	public static final Vector2 ZERO 	= new Vector2(0, 0);
	public static final Vector2 ONE 	= new Vector2(1, 1);

	private float x;
	private float y;

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 one) {
		set(one);
	}

	public Vector2 add(final Vector2 other) {
		return new Vector2(x + other.x, y + other.y);
	}

	public Vector2 add(float x, float y) {
		return new Vector2(this.x + x, this.y + y);
	}

	public Vector2 sub(final Vector2 other) {
		return new Vector2(x - other.x, y - other.y);
	}

	public Vector2 mul(final Vector2 other) {
		return new Vector2(x * other.x, y * other.y);
	}

	public Vector2 mul(float s) {
		return new Vector2(x * s, y * s);
	}

	public Vector2 div(final Vector2 other) {
		return new Vector2(x / other.x, y / other.y);
	}

	public Vector2 div(float s) {
		return new Vector2(x / s, y / s);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float length2() {
		return x * x + y * y;
	}

	public float dot(final Vector2 other) {
		return x * other.x + y * other.y;
	}

	public float cross(final Vector2 other) {
		return x * other.y - y * other.x;
	}

	public Vector2 normalized() {
		float length = length();
		return new Vector2(x / length, y / length);
	}

	public Vector2 rotate(float angle) {
		float rad = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);

		return new Vector2(x * cos - y * sin, x * sin + y * cos);
	}

	public void set(Vector2 one) {
		x = one.x;
		y = one.y;
	}

	/**
	 * THe only method capable of modifying the values of the Vector2.
	 * @param x	x value.
	 * @param y	y value.
	 * @return	this vector for chaining.
	 */
	public Vector2 set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	// Getters.
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public Vector2 copy() {
		return new Vector2(x, y);
	}

	public boolean equals(Vector2 other) {
		return x == other.x && y == other.y;
	}

	public static Vector2 normalized(float x, float y) {
		float length = (float) Math.sqrt(x * x + y * y);
		return new Vector2(x / length, y / length);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Vector2) {
			Vector2 other = (Vector2) obj;
			return x == other.x && y == other.y;
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public float angle() {
		return (float) Math.atan2(y, x);
	}
}
