package com.moka.components;

import com.moka.physics.PhysicsBody;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

public class KinematicBody extends PhysicsBody
{
    @Override
    public BodyType getBodyType()
    {
        return BodyType.KINEMATIC;
    }

    @Override
    protected void defineBody(BodyDef bodyDefinition)
    {

    }
}
