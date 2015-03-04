package com.moka.components;

import com.moka.physics.Collider;
import com.moka.physics.Collision;

public class AABBCollider extends Collider {
	@Override
	public Collision collidesWith(Collider other) {
		return null;
	}

	@Override
	public void response(Collision collision) {

	}
}
