package com.moka.physics;

import com.moka.core.BaseGame;
import com.moka.core.Entity;

import java.util.ArrayList;

public class Physics {
	public Physics() {
		
	}

	public void checkCollisions(BaseGame game) {
		ArrayList<Entity> entities = game.getEntities();
		
		int size = entities.size();

		for(int i = 0; i < size; i++) {
			Entity pivot = entities.get(i);

			// TODO: optimize CircleCollider rotation.
			if(pivot.hasCollider() && pivot.getCollider().isEnabled() && pivot.getTransform().hasChanged()) {
				Collider pCollider = pivot.getCollider();

				for(int j = 0; j < size; j++) {
					if(i == j) continue;
					Entity test = entities.get(j);

					if(test.hasCollider() && test.getCollider().isEnabled()) {
						Collider tCollider = test.getCollider();

						Collision collision = pCollider.collidesWith(tCollider);

						if(collision != null) {
							// collision.
							pCollider.response(collision);
							pivot.collide(collision);
							test.collide(new Collision(pivot, collision.getDirection(), collision.getMagnitude()));
						}
					}
				}
			}
		}
	}
}
