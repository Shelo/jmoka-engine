package com.moka.components;

import com.moka.core.ComponentAttribute;
import com.moka.physics.PhysicsBody;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class RigidBody extends PhysicsBody
{
    private float gravityScale = 1;
    private boolean fixedRotation;

    @Override
    protected void defineFixture(FixtureDef fixture)
    {

    }

    @Override
    protected void defineBody(BodyDef bodyDefinition)
    {
        bodyDefinition.gravityScale = gravityScale;
        bodyDefinition.fixedRotation = fixedRotation;
        bodyDefinition.type = BodyType.DYNAMIC;
    }

    @ComponentAttribute("GravityScale")
    public void setGravityScale(float gravityScale)
    {
        this.gravityScale = gravityScale;
    }

    @ComponentAttribute("FixedRotation")
    public void setFixedRotation(boolean fixedRotation)
    {
        this.fixedRotation = fixedRotation;
    }
}
