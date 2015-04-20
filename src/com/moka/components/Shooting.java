package com.moka.components;

import com.moka.core.Component;
import com.moka.core.Prefab;
import com.moka.triggers.Trigger;
import com.moka.core.xml.XmlAttribute;

public class Shooting extends Component
{
    private Trigger<Prefab> trigger;
    private Prefab bulletPrefab;
    private String button;

    public Shooting()
    {
        // XML Support.
    }

    @Override
    public void onUpdate()
    {
        if (getInput().getButtonDown(button))
        {
            bulletPrefab.setPosition(getTransform().getPosition());
            bulletPrefab.setRotation(getTransform().getLookAngle());
            onFire();
        }
    }

    protected void onFire()
    {
        if (trigger != null)
        {
            trigger.trigger(this, bulletPrefab);
        }
        else
        {
            bulletPrefab.newEntity(null);
        }
    }

    @XmlAttribute("trigger")
    public final void setTrigger(Trigger<Prefab> trigger)
    {
        this.trigger = trigger;
    }

    @XmlAttribute(value = "bulletPrefab", required = true)
    public final void setBulletPrefab(Prefab bulletPrefab)
    {
        this.bulletPrefab = bulletPrefab;
    }

    @XmlAttribute(value = "button", required = true)
    public void setButton(String button)
    {
        this.button = button;
    }
}
