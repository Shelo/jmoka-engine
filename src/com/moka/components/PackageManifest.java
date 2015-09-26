package com.moka.components;

import com.moka.core.Package;
import com.moka.scene.entity.Component;

import java.util.LinkedList;

public class PackageManifest extends Package
{
    @Override
    public String getCommonName()
    {
        return "Moka";
    }

    @Override
    public void registerComponents(LinkedList<Class<? extends Component>> components)
    {
        components.add(Bullet.class);
        components.add(Camera.class);
        components.add(Controllable.class);
        components.add(Debugger.class);
        components.add(Interval.class);
        components.add(LookAt.class);
        components.add(Shooting.class);
        components.add(Sprite.class);
        components.add(RigidBody.class);
        components.add(StaticBody.class);
        components.add(KinematicBody.class);
        components.add(Area.class);
    }
}
