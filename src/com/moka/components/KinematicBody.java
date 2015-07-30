package com.moka.components;

import com.moka.physics.PhysicsBody;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class KinematicBody extends PhysicsBody
{
    @Override
    protected void defineFixture(FixtureDef fixture)
    {

    }

    @Override
    protected void defineBody(BodyDef bodyDefinition)
    {
        bodyDefinition.type = BodyType.KINEMATIC;
    }
}
