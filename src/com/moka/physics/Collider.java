package com.moka.physics;

import com.moka.components.AABBCollider;
import com.moka.components.CircleCollider;
import com.moka.components.Component;
import com.moka.components.SATCollider;
import com.moka.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Collider extends Component {
	public abstract Collision collidesWith(Collider other);
	public abstract void response(Collision collision);

	public static Collision sat(SATCollider box1, SATCollider box2) {
		if (box1.getEntity().getTransform().hasChanged())
			box1.updateData();

		if (box2.getEntity().getTransform().hasChanged())
			box2.updateData();

		ArrayList<Vector2> axes = new ArrayList<>();

		Collections.addAll(axes, box1.getAxes());

		for (Vector2 axis : box2.getAxes())
			if(!axes.contains(axis))
				axes.add(axis);

		float overlap = Float.POSITIVE_INFINITY;
		Vector2 smallest = null;

		System.out.println(axes.size());
		
		for (Vector2 axis : axes) {
			Projection p1 = new Projection(box1.getTVertices(), axis);
			Projection p2 = new Projection(box2.getTVertices(), axis);

			if (!p1.overlaps(p2)) {
				return null;
			} else {
				float o = p1.getOverlap(p2);

				if (Math.abs(o) < Math.abs(overlap)) {
					overlap = o;
					smallest = axis;
				}
			}
		}

		return (smallest != null) ? new Collision(box2.getEntity(), smallest, overlap) : null;
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
