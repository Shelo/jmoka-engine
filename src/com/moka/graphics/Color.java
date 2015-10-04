package com.moka.graphics;

public class Color
{
    public static final Color WHITE = new Color(1, 1, 1, 1);
    public static final Color BLACK = new Color(0, 0, 0, 1);
    public static final Color RED   = new Color(1, 0, 0, 1);
    public static final Color BLUE  = new Color(0, 0, 1, 1);
    public static final Color GREEN = new Color(0, 1, 0, 1);

    public float r;
    public float g;
    public float b;
    public float a;

    public Color(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void set(float r, float g, float b, float a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    // TODO: expand this a little more.
}
