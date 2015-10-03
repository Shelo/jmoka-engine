package com.moka.resources.utils;

import com.moka.scene.entity.Entity;

public class EntityBuffer extends AbstractBuffer<Entity>
{
    public EntityBuffer(int initialCapacity)
    {
        super(initialCapacity);
    }

    public Entity findWithName(String name)
    {
        for (Entity entity : this)
        {
            if (entity.isNamed(name))
                return entity;
        }

        return null;
    }
}
