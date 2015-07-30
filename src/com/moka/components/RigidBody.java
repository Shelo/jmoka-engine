package com.moka.components;

import com.moka.physics.PhysicsBody;
import org.jbox2d.dynamics.BodyType;

public class RigidBody extends PhysicsBody
{
    @Override
    public BodyType getBodyType()
    {
        return BodyType.DYNAMIC;
    }
}
