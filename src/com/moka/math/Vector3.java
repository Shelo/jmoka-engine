package com.moka.math;

public class Vector3
{
    public static final Vector3 AXIS_Z = new Vector3(0, 0, 1);
    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3()
    {
        this(0, 0, 0);
    }

    public Vector3 add(Vector3 o)
    {
        this.x += o.x;
        this.y += o.y;
        this.z += o.z;
        return this;
    }

    public Vector3 add(float x, float y, float z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3 sub(Vector3 o)
    {
        this.x -= o.x;
        this.y -= o.y;
        this.z -= o.z;
        return this;
    }

    public Vector3 mul(Vector3 o)
    {
        this.x *= o.x;
        this.y *= o.y;
        this.z *= o.z;
        return this;
    }

    public Vector3 mul(float o)
    {
        this.x *= o;
        this.y *= o;
        this.z *= o;
        return this;
    }

    public float len()
    {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float sqrLen()
    {
        return x * x + y * y + z * z;
    }

    public float dot(Vector3 o)
    {
        return this.x * o.x + this.y * o.y + this.z * o.z;
    }

    public Vector3 cross(Vector3 o)
    {
        this.x = y * o.z - z * o.y;
        this.y = z * o.x - x * o.z;
        this.z = x * o.y - y * o.x;
        return this;
    }

    public Vector3 nor()
    {
        float len = len();
        this.x /= len;
        this.y /= len;
        this.z /= len;
        return this;
    }

    public Vector3 cpy()
    {
        return new Vector3(x, y, z);
    }

    public void set(Vector3 o)
    {
        this.x = o.x;
        this.y = o.y;
        this.z = o.z;
    }

    public void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
