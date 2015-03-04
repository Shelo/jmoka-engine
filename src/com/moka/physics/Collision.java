package com.moka.physics;

import com.moka.core.Entity;
import com.moka.math.Vector2;

public final class Collision {
    public final Entity entity;
    public final Vector2 direction;
    public final float mag;

    public Collision(Entity entity, Vector2 direction, float mag) {
        this.entity = entity;
        this.direction = direction;
        this.mag = mag;
    }
	
	public Vector2 getMovement() {
		return direction.mul(mag);
	}

	@Override
	public String toString() {
		return String.format("direction: %s", direction.mul(mag).toString());
	}
}
