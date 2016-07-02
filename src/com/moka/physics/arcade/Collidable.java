package com.moka.physics.arcade;

public interface Collidable
{
    boolean collidesWith(AxisAlignedBoundingBox other);
    boolean collidesWith(Circle other);
}
