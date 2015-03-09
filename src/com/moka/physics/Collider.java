package com.moka.physics;

import com.moka.components.AABBCollider;
import com.moka.components.CircleCollider;
import com.moka.components.Component;
import com.moka.components.SATCollider;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Collider extends Component {
	private boolean trigger = false;

	public abstract Collision collidesWith(Collider other);
	public abstract void response(Collision collision);

	public static Collision sat(SATCollider box1, SATCollider box2) {
		if (box1.getEntity().getTransform().hasRotated())
			box1.updateAxes();

		else if (box1.getEntity().getTransform().hasMoved())
			box1.updateVertices();

		if (box2.getEntity().getTransform().hasRotated())
			box2.updateAxes();

		else if (box2.getEntity().getTransform().hasMoved())
			box2.updateVertices();

		ArrayList<Vector2> axes = new ArrayList<>();

		Collections.addAll(axes, box1.getAxes());

		for (Vector2 axis : box2.getAxes())
			if(!axes.contains(axis))
				axes.add(axis);

		float overlap = Float.POSITIVE_INFINITY;
		Vector2 smallest = null;

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
		if (!(
				box1.getBot() >= box2.getTop() ||
				box1.getTop() <= box2.getBot() ||
				box1.getLeft() >= box2.getRight() ||
				box1.getRight() <= box2.getLeft())) {

			float top = box2.getTop() - box1.getBot();
			float bot = box1.getTop() - box2.getBot();
			float left = box1.getRight() - box2.getLeft();
			float right = box2.getRight() - box1.getLeft();

			float y = (top < bot) ? top : 0 - bot;
			float x = (right < left) ? right : 0 - left;

			if (Math.abs(y) < Math.abs(x))
				return new Collision(box2.getEntity(), new Vector2(0, 1), y);

			else
				return new Collision(box2.getEntity(), new Vector2(1, 0), x);
		}

		return null;
	}

	public static Collision circle(CircleCollider circle1, CircleCollider circle2) {
		Vector2 position1 = circle1.getEntity().getTransform().getPosition();
		Vector2 position2 = circle2.getEntity().getTransform().getPosition();

		Vector2 displace = position1.sub(position2);
		float distanceSqrt = displace.length2();

		float radiusSum = circle1.getRadius() + circle2.getRadius();
		float radiusSqrt = radiusSum * radiusSum;

		if(distanceSqrt <= radiusSqrt) {
			float length = (float) (radiusSum - Math.sqrt(distanceSqrt));
			return new Collision(circle2.getEntity(), displace.normalized(), length);
		}

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
		Vector2 circleCenter = circle.getEntity().getTransform().getPosition();

		/* if in corner */
		if
				(circleCenter.x > aabb.getRight() && circleCenter.y > aabb.getTop() ||
				 circleCenter.x < aabb.getLeft() && circleCenter.y > aabb.getTop() ||
				 circleCenter.x > aabb.getRight() && circleCenter.y < aabb.getBot() ||
				 circleCenter.x < aabb.getLeft() && circleCenter.y < aabb.getBot()) {
			return circle(circle, aabb.getBoundingCircle());
		} else {
			return aabb(circle.getBoundingBox(), aabb);
		}
	}

	public static Collision aabbCircle(CircleCollider circle, AABBCollider aabb) {
		return aabbCircle(aabb, circle);
	}

	@XmlAttribute("trigger")
	public void setTrigger(boolean trigger) {
		this.trigger = trigger;
	}

	public boolean isTrigger() {
		return trigger;
	}
}
