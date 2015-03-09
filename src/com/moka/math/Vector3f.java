package com.moka.math;

public class Vector3f {
	public float x;
	public float y;
	public float z;
	
	public Vector3f() {
		
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f add(Vector3f o) {
		this.x += o.x;
		this.y += o.y;
		this.z += o.z;
		return this;
	}
	
	public Vector3f sub(Vector3f o) {
		this.x -= o.x;
		this.y -= o.y;
		this.z -= o.z;
		return this;
	}
	
	public Vector3f mul(Vector3f o) {
		this.x *= o.x;
		this.y *= o.y;
		this.z *= o.z;
		return this;
	}
	
	public Vector3f mul(float o) {
		this.x *= o;
		this.y *= o;
		this.z *= o;
		return this;
	}
	
	public float len() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public float sqrLen() {
		return x * x + y * y + z * z;
	}
	
	public float dot(Vector3f o) {
		return this.x * o.x + this.y * o.y + this.z * o.z;
	}
	
	public Vector3f cross(Vector3f o) {
		this.x = y * o.z - z * o.y;
		this.y = z * o.x - x * o.z;
		this.z = x * o.y - y * o.x;
		return this;
	}
	
	public Vector3f nor() {
		float len = len();
		this.x /= len;
		this.y /= len;
		this.z /= len;
		return this;
	}
	
	public Vector3f cpy() {
		return new Vector3f(x, y, z);
	}
}
