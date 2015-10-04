package com.moka.components;

import com.moka.physics.PhysicsBody;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.scene.entity.Entity;
import com.moka.triggers.Trigger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

public class Area extends PhysicsBody
{
    private Trigger<Entity> onEnterTrigger;
    private Trigger<Entity> onExitTrigger;

    @Override
    public void sync()
    {
        float newX = toPos(getTransform().getPosition().x);
        float newY = toPos(getTransform().getPosition().y);

        getBody().setLinearVelocity(new Vec2(1, 1));
        getBody().setTransform(new Vec2(newX, newY), 0);
    }

    @Override
    protected void defineFixture(FixtureDef fixture)
    {
        fixture.isSensor = true;
    }

    @Override
    protected void defineBody(BodyDef bodyDefinition)
    {
        bodyDefinition.type = BodyType.KINEMATIC;
    }

    public void onEnter(Entity entity)
    {
        callTrigger(onEnterTrigger, entity);
    }

    public void onExit(Entity entity)
    {
        callTrigger(onExitTrigger, entity);
    }

    @ComponentAttribute("OnEnter")
    public void setOnEnterTrigger(Trigger<Entity> trigger)
    {
        onEnterTrigger = trigger;
    }

    @ComponentAttribute("OnExit")
    public void setOnExitTrigger(Trigger<Entity> trigger)
    {
        onExitTrigger = trigger;
    }

    @Override
    public void onCollide(Entity other, Contact contact)
    {
        // Do nothing.
    }
}
