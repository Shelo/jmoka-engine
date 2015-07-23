package com.moka.components;

import com.moka.core.ComponentAttribute;
import com.moka.physics.Collider;
import com.moka.physics.Collision;

public class AABBCollider extends Collider
{
	private float offsetX;
	private float offsetY;
	private float width;
	private float height;

	private boolean shouldInitWidth = true;
	private boolean shouldInitHeight = true;

	private CircleCollider boundingCircle;

	@Override
	public void onCreate()
	{
		if (shouldInitWidth)
		{
			width = getTransform().getSize().x;
		}

		if (shouldInitHeight)
		{
			height = getTransform().getSize().y;
		}
	}

	@ComponentAttribute("offsetX")
	public void setOffsetX(float offx)
	{
		this.offsetX = offx;
	}

	@ComponentAttribute("offsetY")
	public void setOffsetY(float offy)
	{
		this.offsetY = offy;
	}

	@ComponentAttribute("width")
	public void setWith(float width)
	{
		shouldInitWidth = false;
		this.width = width;
	}

	@ComponentAttribute("height")
	public void setHeight(float height)
	{
		shouldInitHeight = false;
		this.height = height;
	}

	public float getTop()
	{
		return getTransform().getPosition().y + height / 2;
	}

	public float getBottom()
	{
		return getTransform().getPosition().y + offsetY - height / 2;
	}

	public float getLeft()
	{
		return getTransform().getPosition().x + offsetX - width / 2;
	}

	public float getRight()
	{
		return getTransform().getPosition().x + width / 2;
	}

	@Override
	public Collision collidesWith(Collider other)
	{
		if (other instanceof AABBCollider)
		{
			return Collider.aabb(this, (AABBCollider) other);
		}
		else if (other instanceof CircleCollider)
		{
			return Collider.aabbCircle(this, (CircleCollider) other);
		}

		return null;
	}

	@Override
	public void response(Collision collision)
	{
		getTransform().move(collision.getMovement());
	}

	public CircleCollider getBoundingCircle()
	{
		if (boundingCircle == null)
		{
			boundingCircle = new CircleCollider();
			boundingCircle.setEntity(entity());
			boundingCircle.setRadius((float) Math.sqrt(width * width + height * height) / 2);
		}

		return boundingCircle;
	}
}
