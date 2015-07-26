package com.moka.physics;

import com.moka.core.entity.Entity;
import com.moka.core.SubEngine;

import java.util.ArrayList;

public class Physics extends SubEngine
{
    public void checkCollisions()
    {
        ArrayList<Entity> entities = getContext().getAllEntities();

        int size = entities.size();

        for (int i = 0; i < size; i++)
        {
            Entity pivot = entities.get(i);

            // TODO: optimize CircleCollider rotation.
            if (pivot.hasCollider() && pivot.getCollider().isEnabled() && pivot.getTransform().hasChanged())
            {
                Collider pCollider = pivot.getCollider();

                // TODO: think if this is right.
                for (int j = 0; j < size; j++)
                {
                    if (i == j)
                    {
                        continue;
                    }

                    Entity test = entities.get(j);

                    if (test.hasCollider() && test.getCollider().isEnabled())
                    {
                        Collider tCollider = test.getCollider();
                        Collision collision = pCollider.internalCollides(tCollider);

                        if (collision != null)
                        {
                            // collision.
                            if (!pCollider.isTrigger())
                            {
                                pCollider.response(collision);
                            }

                            pCollider.onCollide(tCollider.getEntity(), collision);
                            tCollider.onCollide(new Collision(pivot, collision));
                        }
                    }
                }
            }
        }
    }
}
