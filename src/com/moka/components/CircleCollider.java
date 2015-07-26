package com.moka.components;

import com.moka.core.ComponentAttribute;
import com.moka.physics.Collider;
import com.moka.physics.Collision;

public class CircleCollider extends Collider
{
	private float radius;
	private AABBCollider boundingBox;

	@Override
	public void onCreate()
	{
		if(radius == 0)
		{
			float width  = getEntity().getSprite().getWidth();
			float height = getEntity().getSprite().getHeight();
			radius = (width + height) / 4;
		}
	}

	@Override
	public Collision collidesWith(Collider other)
	{
		if(other instanceof CircleCollider)
		{
			return circle(this, (CircleCollider) other);
		}
		else if (other instanceof AABBCollider)
		{
			return aabbCircle(this, (AABBCollider) other);
		}

		return null;
	}

	@Override
	public void response(Collision collision)
	{
		getTransform().move(collision.getMovement());
	}

	@ComponentAttribute("Radius")
	public void setRadius(float radius)
	{
		this.radius = radius;
	}

	public float getRadius()
	{
		return radius;
	}

	public AABBCollider getBoundingBox()
	{
		if (boundingBox == null)
		{
            boundingBox = new AABBCollider();
			boundingBox.setEntity(getEntity());
            boundingBox.setCollisionTrigger(getCollisionTrigger());
			boundingBox.setWith(radius * 2);
			boundingBox.setHeight(radius * 2);
		}

		return boundingBox;
	}
}
