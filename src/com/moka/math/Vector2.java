package com.moka.math;

/**
 * 2D Vector used by the engine, this implementation will always return this vector but modified.
 * This approach will help reduce the creation of new instances of vectors, is recommended to use
 * buffer vector objects.
 *
 * @author Shelo
 */
public class Vector2
{
    public static final Vector2 LEFT = new Vector2(-1, 0);
    public static final Vector2 RIGHT = new Vector2(1, 0);
    public static final Vector2 DOWN = new Vector2(0, -1);
    public static final Vector2 UP = new Vector2(0, 1);
    public static final Vector2 LEFT_UP    = new Vector2(-1, 1).nor();
    public static final Vector2 LEFT_DOWN  = new Vector2(-1, -1).nor();
    public static final Vector2 RIGHT_UP   = new Vector2(1, 1).nor();
    public static final Vector2 RIGHT_DOWN = new Vector2(1, -1).nor();
    public static final Vector2 ZERO       = new Vector2(0, 0);

    public float x;
    public float y;

    /**
     * Creates a new vector with (0, 0) values.
     */
    public Vector2()
    {

    }

    /**
     * Creates a vector by a given direction.
     *
     * @param x x's direction.
     * @param y y's direction.
     */
    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new vector with the same values of other vector.
     */
    public Vector2(Vector2 other)
    {
        this(other.x, other.y);
    }

    public Vector2 add(Vector2 o)
    {
        this.x += o.x;
        this.y += o.y;
        return this;
    }

    public Vector2 add(float x, float y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2 sub(Vector2 o)
    {
        this.x -= o.x;
        this.y -= o.y;
        return this;
    }

    public Vector2 mul(Vector2 o)
    {
        this.x *= o.x;
        this.y *= o.y;
        return this;
    }

    public Vector2 mul(float o)
    {
        this.x *= o;
        this.y *= o;
        return this;
    }

    public float dot(Vector2 o)
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

    public Vector2 nor()
    {
        float len = len();
        this.x /= len;
        this.y /= len;
        return this;
    }

    public Vector2 cpy()
    {
        return new Vector2(x, y);
    }

    /**
     * Rotate this vector by a given angle.
     *
     * @param radians angle in radians.
     * @return this vector.
     */
    public Vector2 rotate(float radians)
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

    public Vector2 set(Vector2 o)
    {
        this.x = o.x;
        this.y = o.y;
        return this;
    }

    public Vector2 set(float x, float y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Vector2)
        {
            Vector2 other = (Vector2) obj;
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
