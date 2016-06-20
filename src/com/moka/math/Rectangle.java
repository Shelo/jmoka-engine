package com.moka.math;

public class Rectangle
{
    public float left;
    public float top;
    public float width;
    public float height;

    public Rectangle(float left, float top, float width, float height)
    {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public Rectangle()
    {
        this(0, 0, 0, 0);
    }

    public Rectangle add(Rectangle o)
    {
        this.left += o.left;
        this.top += o.top;
        this.width += o.width;
        this.height += o.height;
        return this;
    }

    public Rectangle add(float x, float y, float z)
    {
        this.left += x;
        this.top += y;
        this.width += z;
        this.height += height;
        return this;
    }

    public Rectangle sub(Rectangle o)
    {
        this.left -= o.left;
        this.top -= o.top;
        this.width -= o.width;
        this.height -= o.height;
        return this;
    }

    public Rectangle mul(Rectangle o)
    {
        this.left *= o.left;
        this.top *= o.top;
        this.width *= o.width;
        this.height *= o.height;
        return this;
    }

    public Rectangle mul(float o)
    {
        this.left *= o;
        this.top *= o;
        this.width *= o;
        this.height *= o;
        return this;
    }

    public float len()
    {
        return (float) Math.sqrt(sqrLen());
    }

    public float sqrLen()
    {
        return left * left + top * top + width * width + height * height;
    }

    public float dot(Rectangle o)
    {
        return left * o.left + top * o.top + width * o.width + height * o.height;
    }

    public Rectangle nor()
    {
        float len = len();
        this.left /= len;
        this.top /= len;
        this.width /= len;
        this.height /= len;
        return this;
    }

    public Rectangle cpy()
    {
        return new Rectangle(left, top, width, height);
    }

    public void set(Rectangle o)
    {
        this.left = o.left;
        this.top = o.top;
        this.width = o.width;
        this.height = o.height;
    }

    public void set(float x, float y, float z, float w)
    {
        this.left = x;
        this.top = y;
        this.width = z;
        this.height = w;
    }
}
