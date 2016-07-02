package com.moka.physics.arcade;

public class Circle implements Collidable
{
    private float x;
    private float y;
    private float radius;

    public Circle(float x, float y, float radius)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public boolean collidesWith(AxisAlignedBoundingBox other)
    {
        return false;
    }

    @Override
    public boolean collidesWith(Circle other)
    {
        float dx = x - other.x;
        float dy = y - other.y;

        float circlesRadius = radius + other.radius;

        return dx * dx + dy * dy < circlesRadius * circlesRadius;
    }
}
