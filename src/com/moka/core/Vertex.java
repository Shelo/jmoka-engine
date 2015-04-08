package com.moka.core;

import com.moka.math.Vector2f;

public class Vertex
{
    public static final int SIZE = 5;

    private final Vector2f xy;
    private float z;
    private float s;
    private float t;

    public Vertex(float x, float y, float z, float s, float t)
    {
        xy = new Vector2f(x, y);
        this.z = z;
        this.s = s;
        this.t = t;
    }

    public float getZ()
    {
        return z;
    }

    public float getY()
    {
        return xy.y;
    }

    public float getX()
    {
        return xy.x;
    }

    public float getS()
    {
        return s;
    }

    public float getT()
    {
        return t;
    }

    public Vector2f asVector2()
    {
        return xy;
    }
}
