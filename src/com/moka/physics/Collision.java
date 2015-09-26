package com.moka.physics;

import com.moka.scene.entity.Entity;
import org.jbox2d.dynamics.contacts.Contact;

public final class Collision
{
    private Contact contact;
    private Entity other;

    public Collision(Entity other, Contact contact)
    {
        this.other = other;
        this.contact = contact;
    }

    public Entity getOther()
    {
        return other;
    }

    public Contact getContact()
    {
        return contact;
    }
}
