package com.moka.physics;

import com.moka.core.entity.Entity;
import com.moka.math.Vector2;

public final class Collision
{
    private final Vector2 direction;
    private final float magnitude;
    private final boolean guilty;
    private final Entity entity;

    /**
     * Creates a new collision information wrapper.
     *
     * @param entity       the getEntity with which I collided.
     * @param norDirection the normalized direction of the collision.
     * @param magnitude    the magnitude collision.
     */
    public Collision(Entity entity, Vector2 norDirection, float magnitude)
    {
        this.entity = entity;
        this.direction = norDirection;
        this.magnitude = magnitude;
        this.guilty = true;
    }

    public Collision(Entity entity, Collision collision)
    {
        this.entity = entity;
        this.direction = collision.direction.cpy();
        this.magnitude = collision.magnitude;
        this.guilty = false;
    }

    public boolean isGuilty()
    {
        return guilty;
    }

    public Vector2 getMovement()
    {
        return direction.mul(magnitude);
    }

    @Override
    public String toString()
    {
        return String.format("direction: %s", direction.mul(magnitude).toString());
    }

    public Entity getEntity()
    {
        return entity;
    }

    public Vector2 getDirection()
    {
        return direction;
    }

    public float getMagnitude()
    {
        return magnitude;
    }
}
