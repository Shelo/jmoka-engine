package com.moka.components;

import com.moka.physics.PhysicsBody;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

public class StaticBody extends PhysicsBody
{
    @Override
    public BodyType getBodyType()
    {
        return BodyType.STATIC;
    }

    @Override
    protected void defineBody(BodyDef bodyDefinition)
    {

    }
}
