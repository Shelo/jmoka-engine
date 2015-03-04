package com.moka.graphics;

public class Color {
	public static final Color WHITE 	= new Color(1, 1, 1, 1);
	public static final Color BLACK 	= new Color(0, 0, 0, 1);
	public static final Color RED 		= new Color(1, 0, 0, 1);
	public static final Color BLUE 		= new Color(0, 0, 1, 1);
	public static final Color GREEN 	= new Color(0, 1, 0, 1);

	private float r;
	private float g;
	private float b;
	private float a;

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public float getR() {
		return r;
	}

	public float getG() {
		return g;
	}

	public float getB() {
		return b;
	}

	public float getA() {
		return a;
	}
}
