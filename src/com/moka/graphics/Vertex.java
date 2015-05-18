package com.moka.graphics;

import com.moka.math.Vector2;

public class Vertex
{
    public static final int SIZE = 4;

    private final Vector2 xy;
    private float s;
    private float t;

    public Vertex(float x, float y, float s, float t)
    {
        xy = new Vector2(x, y);
        this.s = s;
        this.t = t;
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

    public Vector2 getXY()
    {
        return xy;
    }
}
