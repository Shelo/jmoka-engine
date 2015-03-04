package com.moka.components;

import com.moka.physics.Collider;

public class SATCollider extends Collider {
	@Override
	public boolean collidesWith(CircleCollider circle) {
		return false;
	}

	@Override
	public boolean collidesWith(AABBCollider rect) {
		return false;
	}

	@Override
	public boolean collidesWith(SATCollider sat) {
		return false;
	}
}
