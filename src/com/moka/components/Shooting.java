package com.moka.components;

import com.moka.core.Component;
import com.moka.core.Prefab;
import com.moka.core.triggers.Trigger;
import com.moka.core.xml.XmlAttribute;
import org.lwjgl.glfw.GLFW;

public class Shooting extends Component
{
    private Trigger<Prefab> trigger;
    private Prefab bulletPrefab;

    public Shooting()
    {
        // XML Support.
    }

    @Override
    public void onUpdate()
    {
        if (getInput().getKeyDown(GLFW.GLFW_KEY_SPACE))
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
}
