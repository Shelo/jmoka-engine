package com.moka.math;

/**
 * 2D Vector used by the engine, this implementation will always return this vector but modified.
 * This approach will help reduce the creation of new instances of vectors, is recommended to use
 * buffer vector objects.
 *
 * @author Shelo
 */
public class Vector2f
{
    public static final Vector2f LEFT = new Vector2f(-1, 0);
    public static final Vector2f RIGHT = new Vector2f(1, 0);
    public static final Vector2f DOWN = new Vector2f(0, -1);
    public static final Vector2f UP = new Vector2f(0, 1);
    public static final Vector2f LEFT_UP    = new Vector2f(-1, 1).nor();
    public static final Vector2f LEFT_DOWN  = new Vector2f(-1, -1).nor();
    public static final Vector2f RIGHT_UP   = new Vector2f(1, 1).nor();
    public static final Vector2f RIGHT_DOWN = new Vector2f(1, -1).nor();
    public static final Vector2f ZERO       = new Vector2f(0, 0);

    public float x;
    public float y;

    /**
     * Creates a new vector with (0, 0) values.
     */
    public Vector2f()
    {

    }

    /**
     * Creates a vector by a given direction.
     *
     * @param x x's direction.
     * @param y y's direction.
     */
    public Vector2f(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2f add(Vector2f o)
    {
        this.x += o.x;
        this.y += o.y;
        return this;
    }

    public Vector2f add(float x, float y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2f sub(Vector2f o)
    {
        this.x -= o.x;
        this.y -= o.y;
        return this;
    }

    public Vector2f mul(Vector2f o)
    {
        this.x *= o.x;
        this.y *= o.y;
        return this;
    }

    public Vector2f mul(float o)
    {
        this.x *= o;
        this.y *= o;
        return this;
    }

    public float dot(Vector2f o)
    {
        return this.x * o.x + this.y * o.y;
    }

    public float len()
    {
        return (float) Math.sqrt((x * x + y * y));
    }

    public float sqrLen()
    {
        return (x * x + y * y);
    }

    public Vector2f nor()
    {
        float len = len();
        this.x /= len;
        this.y /= len;
        return this;
    }

    public Vector2f cpy()
    {
        return new Vector2f(x, y);
    }

    /**
     * Rotate this vector by a given angle.
     *
     * @param radians angle in radians.
     * @return this vector.
     */
    public Vector2f rotate(float radians)
    {
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);
        x = x * cos - y * sin;
        y = x * sin + y * cos;
        return this;
    }

    /**
     * Calculate the angle of this vector.
     *
     * @return the angle in radians.
     */
    public float angle()
    {
        return (float) Math.atan2(y, x);
    }

    public Vector2f set(Vector2f o)
    {
        this.x = o.x;
        this.y = o.y;
        return this;
    }

    public Vector2f set(float x, float y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Vector2f)
        {
            Vector2f other = (Vector2f) obj;
            return this.x == other.x && this.y == other.y;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
