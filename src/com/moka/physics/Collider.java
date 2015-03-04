package com.moka.physics;

import com.moka.components.AABBCollider;
import com.moka.components.CircleCollider;
import com.moka.components.Component;
import com.moka.components.SATCollider;

public abstract class Collider extends Component {
	public abstract boolean collidesWith(CircleCollider circle);
	public abstract boolean collidesWith(AABBCollider rect);
	public abstract boolean collidesWith(SATCollider sat);

	public static Collision sat(SATCollider box1, SATCollider box2) {
		return null;
	}

	public static Collision aabb(AABBCollider box1, AABBCollider box2) {
		return null;
	}

	public static Collision circle(CircleCollider circle1, CircleCollider circle2) {
		return null;
	}

	// SAT-AABB
	public static Collision satAABB(SATCollider sat, AABBCollider aabb) {
		return null;
	}

	public static Collision satAABB(AABBCollider aabb, SATCollider sat) {
		return satAABB(sat, aabb);
	}

	// SAT-CIRCLE
	public static Collision satCircle(SATCollider sat, CircleCollider circle) {
		return null;
	}

	public static Collision satCircle(CircleCollider circle, SATCollider sat) {
		return satCircle(sat, circle);
	}

	// AABB-Circle
	public static Collision aabbCircle(AABBCollider aabb, CircleCollider circle) {
		return null;
	}

	public static Collision aabbCircle(CircleCollider circle, AABBCollider aabb) {
		return aabbCircle(aabb, circle);
	}
}
