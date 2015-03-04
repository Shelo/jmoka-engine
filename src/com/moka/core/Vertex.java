package com.moka.core;

import com.moka.math.Vector2;

public class Vertex {
	public static final int SIZE = 5;

	private float x;
	private float y;
	private float z;
	private float s;
	private float t;

	public Vertex(float x, float y, float z, float s, float t) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.s = s;
		this.t = t;
	}

	public float getZ() {
		return z;
	}

	public float getY() {
		return y;
	}

	public float getX() {
		return x;
	}

	public float getS() {
		return s;
	}

	public float getT() {
		return t;
	}

	public Vector2 asVector2() {
		return new Vector2(x, y);
	}
}
