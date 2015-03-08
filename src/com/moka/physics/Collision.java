package com.moka.physics;

import com.moka.core.Entity;
import com.moka.math.Vector2;

public final class Collision {
    public final Entity entity;
    public final Vector2 direction;
    public final float magnitude;

	/**
	 * Creates a new collision information wrapper.
	 * @param entity		the entity with which I collided.
	 * @param norDirection	the normalized direction of the collision.
	 * @param magnitude		the magnitude collision.
	 */
    public Collision(Entity entity, Vector2 norDirection, float magnitude) {
        this.entity = entity;
        this.direction = norDirection;
        this.magnitude = magnitude;
    }

	public Collision(Entity entity, Collision collision) {
		this.entity = entity;
		this.direction = collision.direction.copy();
		this.magnitude = collision.magnitude;
	}

	public Vector2 getMovement() {
		return direction.mul(magnitude);
	}

	@Override
	public String toString() {
		return String.format("direction: %s", direction.mul(magnitude).toString());
	}

	public Entity getEntity() {
		return entity;
	}

	public Vector2 getDirection() {
		return direction;
	}

	public float getMagnitude() {
		return magnitude;
	}
}
