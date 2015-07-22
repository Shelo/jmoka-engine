package com.moka.physics;

import com.moka.components.AABBCollider;
import com.moka.components.CircleCollider;
import com.moka.components.SatCollider;
import com.moka.core.entity.Component;
import com.moka.core.ComponentAttribute;
import com.moka.math.Vector2;
import com.moka.triggers.Trigger;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Collider extends Component
{
    private boolean trigger = false;
    private static Vector2 buf = new Vector2();

    private Trigger<Collision> collisionTrigger;

    public void onCollide(Collision collision)
    {
        if (collisionTrigger != null)
        {
            collisionTrigger.trigger(this, collision);
        }
    }

    public static Collision sat(SatCollider box1, SatCollider box2)
    {
        if (box1.entity().getTransform().hasRotated())
        {
            box1.updateAxes();
        }

        else if (box1.entity().getTransform().hasMoved())
        {
            box1.updateVertices();
        }

        if (box2.entity().getTransform().hasRotated())
        {
            box2.updateAxes();
        }

        else if (box2.entity().getTransform().hasMoved())
        {
            box2.updateVertices();
        }

        ArrayList<Vector2> axes = new ArrayList<>();

        Collections.addAll(axes, box1.getAxes());

        for (Vector2 axis : box2.getAxes())
        {
            if (!axes.contains(axis))
            {
                axes.add(axis);
            }
        }

        float overlap = Float.POSITIVE_INFINITY;
        Vector2 smallest = null;

        for (Vector2 axis : axes)
        {
            Projection p1 = new Projection(box1.getTVertices(), axis);
            Projection p2 = new Projection(box2.getTVertices(), axis);

            if (!p1.overlaps(p2))
            {
                return null;
            }
            else
            {
                float o = p1.getOverlap(p2);

                if (Math.abs(o) < Math.abs(overlap))
                {
                    overlap = o;
                    smallest = new Vector2(axis);
                }
            }
        }

        return (smallest != null) ? new Collision(box2.entity(), smallest, overlap) : null;
    }

    public static Collision aabb(AABBCollider box1, AABBCollider box2)
    {
        if (!(box1.getBottom() >= box2.getTop() ||
                box1.getTop() <= box2.getBottom() ||
                box1.getLeft() >= box2.getRight() ||
                box1.getRight() <= box2.getLeft()))
        {

            float top = box2.getTop() - box1.getBottom();
            float bot = box1.getTop() - box2.getBottom();
            float left = box1.getRight() - box2.getLeft();
            float right = box2.getRight() - box1.getLeft();

            float y = (top < bot) ? top : 0 - bot;
            float x = (right < left) ? right : 0 - left;

            if (Math.abs(y) < Math.abs(x))
            {
                return new Collision(box2.entity(), new Vector2(0, 1), y);
            }
            else
            {
                return new Collision(box2.entity(), new Vector2(1, 0), x);
            }
        }

        return null;
    }

    public static Collision circle(CircleCollider circle1, CircleCollider circle2)
    {
        Vector2 position1 = circle1.entity().getTransform().getPosition();
        Vector2 position2 = circle2.entity().getTransform().getPosition();

        Vector2 displace = buf.set(position1).sub(position2);
        float distanceSqrt = displace.sqrLen();

        float radiusSum = circle1.getRadius() + circle2.getRadius();
        float radiusSqrt = radiusSum * radiusSum;

        if (distanceSqrt <= radiusSqrt)
        {
            float length = (float) (radiusSum - Math.sqrt(distanceSqrt));
            return new Collision(circle2.entity(), displace.nor(), length);
        }

        return null;
    }

    // SAT-AABB
    public static Collision satAABB(SatCollider sat, AABBCollider aabb)
    {
        return null;
    }

    public static Collision satAABB(AABBCollider aabb, SatCollider sat)
    {
        return satAABB(sat, aabb);
    }

    // SAT-CIRCLE
    public static Collision satCircle(SatCollider sat, CircleCollider circle)
    {
        return null;
    }

    public static Collision satCircle(CircleCollider circle, SatCollider sat)
    {
        return satCircle(sat, circle);
    }

    // AABB-Circle
    public static Collision aabbCircle(AABBCollider aabb, CircleCollider circle)
    {
        Vector2 circleCenter = circle.entity().getTransform().getPosition();

		/* if in corner */
        if (circleCenter.x > aabb.getRight() && circleCenter.y > aabb.getTop() ||
                circleCenter.x < aabb.getLeft() && circleCenter.y > aabb.getTop() ||
                circleCenter.x > aabb.getRight() && circleCenter.y < aabb.getBottom() ||
                circleCenter.x < aabb.getLeft() && circleCenter.y < aabb.getBottom())
        {
            return circle(circle, aabb.getBoundingCircle());
        }
        else
        {
            return aabb(circle.getBoundingBox(), aabb);
        }
    }

    public static Collision aabbCircle(CircleCollider circle, AABBCollider aabb)
    {
        return aabbCircle(aabb, circle);
    }

    @ComponentAttribute("collisionTrigger")
    public void setCollisionTrigger(Trigger<Collision> collisionTrigger)
    {
        this.collisionTrigger = collisionTrigger;
    }

    @ComponentAttribute("isTrigger")
    public void setTrigger(boolean trigger)
    {
        this.trigger = trigger;
    }

    public boolean isTrigger()
    {
        return trigger;
    }

    public abstract void response(Collision collision);
    public abstract Collision collidesWith(Collider other);
}
