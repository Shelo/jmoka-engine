package com.moka.core;

import com.moka.exceptions.JMokaException;
import com.moka.math.*;

public final class Transform
{
    private final Entity entity;

    private boolean useOwnSize = false;
    private Quaternion prevRotation;
    private Vector2f prevPosition;
    private Quaternion rotation;
    private Vector2f position;
    private Vector2f prevSize;
    private Vector2f size;

    public Transform(Entity entity)
    {
        this.entity = entity;

        prevRotation = new Quaternion();
        rotation = new Quaternion();
        prevPosition = new Vector2f();
        position = new Vector2f();
        prevSize = new Vector2f();
        size = new Vector2f();
    }

    public void update()
    {
        prevPosition.set(position);
        prevRotation.set(rotation);
        prevSize.set(getSize());
    }

    public void move(float x, float y)
    {
        position.add(x, y);
    }

    public Quaternion getRotation()
    {
        return rotation;
    }

    public Vector2f getSize()
    {
        Vector2f rSize;

        if (!useOwnSize)
        {
            if (entity.hasSprite())
            {
                rSize = entity.getSprite().getSize();

                if (rSize == null)
                {
                    throw new JMokaException("Entity has no dimensions!");
                }

            }
            else
            {
                rSize = size;
            }
        }
        else
        {
            rSize = size;
        }

        return rSize;
    }

    public void setRotation(Quaternion rotation)
    {
        this.rotation = rotation;
    }

    public void setRotation(float radians)
    {
        this.rotation = new Quaternion(Vector3f.AXIS_Z, radians);
    }

    public void setRotationDeg(float rotation)
    {
        this.rotation.set(new Quaternion(Vector3f.AXIS_Z, (float) Math.toRadians(rotation)));
    }

    public void setSize(float x, float y)
    {
        useOwnSize = true;
        size.set(x, y);
    }

    public void setSize(Vector2f size)
    {
        setSize(size.x, size.y);
    }

    public void setPosition(float x, float y)
    {
        position.set(x, y);
    }

    public void setPosition(Vector2f position)
    {
        this.position.set(position);
    }

    public float getPositionX()
    {
        return position.x;
    }

    public float getPositionY()
    {
        return position.y;
    }

    public float getPositionZ()
    {
        return 0;
    }

    public boolean hasRotated()
    {
        return !prevRotation.equals(rotation);
    }

    public boolean hasMoved()
    {
        return !prevPosition.equals(position);
    }

    public boolean hasChanged()
    {
        return hasRotated() || hasMoved();
    }

    public Vector2f getPosition()
    {
        return position;
    }

    public void move(Vector2f movement)
    {
        move(movement.x, movement.y);
    }
}
