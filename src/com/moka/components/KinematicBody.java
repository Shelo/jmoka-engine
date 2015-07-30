package com.moka.components;

import com.moka.physics.PhysicsBody;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

public class KinematicBody extends PhysicsBody
{
    @Override
    public BodyType getBodyType()
    {
        return BodyType.KINEMATIC;
    }

    @Override
    public void onUpdate()
    {
        getBodyDefinition().position = new Vec2(toPos(getTransform().getPosition().x),
                toPos(getTransform().getPosition().y));
        getBodyDefinition().angle = getTransform().getLookAngle();
    }
}
