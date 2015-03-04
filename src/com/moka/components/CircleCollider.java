package com.moka.components;

import com.moka.physics.Collider;
import com.moka.physics.Collision;

public class CircleCollider extends Collider {
	@Override
	public Collision collidesWith(CircleCollider circle) {
		return null;
	}

	@Override
	public Collision collidesWith(AABBCollider rect) {
		return null;
	}

	@Override
	public Collision collidesWith(SATCollider sat) {
		return null;
	}
}
