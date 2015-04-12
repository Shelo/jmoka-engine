package com.moka.core;

import com.moka.math.Vector2f;
import com.moka.math.Matrix3;
import com.moka.utils.JMokaException;

/**
 * New transform to test stuff, this should replace the normal Transform.
 *
 * TODO: change comment.
 *
 * @author shelo
 */
public class Transform
{
    /**
     * Entity that has this transform.
     */
    private final Entity entity;

    /**
     * Rotation stored as a matrix, for optimization porpoises.
     */
    private Matrix3 rotation;

    /**
     * Position that describes the transform.
     */
    private Vector2f position;

    /**
     * Size that describes the transform.
     */
    private Vector2f size;

    /**
     * If we should use our size or an sprite size.
     */
    private boolean useOwnSize;

    /**
     * Save a previous state in order to check changes.
     */
    private Transform prev = null;

    public Transform()
    {
        this.entity = null;

        rotation = new Matrix3();
        position = new Vector2f();
        size = new Vector2f();
    }

    public Transform(Entity entity)
    {
        this.entity = entity;

        rotation = new Matrix3();
        position = new Vector2f();
        size = new Vector2f();

        prev = new Transform();
    }

    /**
     * Updates the previous transform in order to catch up.
     */
    public void update()
    {
        prev.set(this);
    }

    public void move(float x, float y)
    {
        position.add(x, y);
    }

    public void move(Vector2f distance)
    {
        position.add(distance);
    }

    private void set(Transform other)
    {
        position.set(other.getPosition());
        rotation.set(other.getRotation());
        size.set(other.getSize());
        useOwnSize = other.useOwnSize;
    }

    public void setSize(Vector2f size)
    {
        useOwnSize = true;
        this.size = size;
    }

    public void setPosition(Vector2f position)
    {
        this.position = position;
    }

    public void setRotation(float radians)
    {
        this.rotation.toRotation(radians);
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

    public Matrix3 getRotation()
    {
        return rotation;
    }

    public Vector2f getPosition()
    {
        return position;
    }

    /**
     * Gets the entity that has this transform. That entity will never
     * change, since the entity is declared as a final.
     *
     * @return the entity.
     */
    public Entity getEntity()
    {
        return entity;
    }

    public boolean hasRotated()
    {
        return !prev.getRotation().equals(rotation);
    }

    public boolean hasMoved()
    {
        return !prev.getPosition().equals(position);
    }

    public boolean hasChanged()
    {
        return hasRotated() || hasMoved();
    }
}
