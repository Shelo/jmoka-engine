package com.moka.physics.arcade;

public class AxisAlignedBoundingBox implements Collidable
{
    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    public AxisAlignedBoundingBox(float minX, float minY, float maxX, float maxY)
    {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    public boolean collidesWith(AxisAlignedBoundingBox other)
    {
        if (maxX < other.minX || minX > maxX) {
            return false;
        }

        if (maxY < other.minY || minY > maxY) {
            return false;
        }

        return true;
    }

    @Override
    public boolean collidesWith(Circle other)
    {
        return false;
    }
}
