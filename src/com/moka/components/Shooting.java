package com.moka.components;

import com.moka.core.entity.Component;
import com.moka.core.Prefab;
import com.moka.core.ComponentAttribute;
import com.moka.triggers.Trigger;

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

    @ComponentAttribute("trigger")
    public final void setTrigger(Trigger<Prefab> trigger)
    {
        this.trigger = trigger;
    }

    @ComponentAttribute(value = "bulletPrefab", required = true)
    public final void setBulletPrefab(Prefab bulletPrefab)
    {
        this.bulletPrefab = bulletPrefab;
    }

    @ComponentAttribute(value = "button", required = true)
    public void setButton(String button)
    {
        this.button = button;
    }
}
