package com.moka.physics;

import com.moka.core.SubEngine;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.ArrayList;

public class Physics extends SubEngine implements ContactListener
{
    private World world;
    private Vec2 gravity = new Vec2(0, - 10.0f);
    private ArrayList<PhysicsBody> physicsBodies;

    public void create()
    {
        world = new World(gravity);
        world.setAllowSleep(true);

        physicsBodies = new ArrayList<>();
    }

    public void simulate()
    {
        world.step(getTime().getFixedDelta(), 6, 3);
        world.setContactListener(this);

        for (PhysicsBody physicsBody : physicsBodies)
        {
            physicsBody.fixedUpdate();
        }
    }

    public void setGravity(float x, float y)
    {
        gravity.set(x, y);
    }

    public Vec2 getGravity()
    {
        return gravity;
    }

    public Body add(PhysicsBody physicsBody)
    {
        physicsBodies.add(physicsBody);
        Body body = world.createBody(physicsBody.getBodyDefinition());
        body.createFixture(physicsBody.getFixture());
        return body;
    }

    @Override
    public void beginContact(Contact contact)
    {
        PhysicsBody bodyA = (PhysicsBody) contact.getFixtureA().getUserData();
        PhysicsBody bodyB = (PhysicsBody) contact.getFixtureB().getUserData();

        bodyA.onCollide(bodyB.getEntity(), contact);
        bodyB.onCollide(bodyA.getEntity(), contact);
    }

    @Override
    public void endContact(Contact contact)
    {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold)
    {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse)
    {

    }
}
