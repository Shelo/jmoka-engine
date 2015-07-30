package example.scenes;

import com.moka.components.RigidBody;
import com.moka.physics.PhysicsBody;
import com.moka.core.Scene;
import com.moka.core.entity.Entity;
import example.Res;
import org.jbox2d.dynamics.BodyType;

public class PhysicsScene extends Scene
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);

        Res.prefabs.physics1.newEntity("Ball", 305, 450);
        Res.prefabs.physics2.newEntity("Floor", 300, 200);
    }

    @Override
    public void onLeave()
    {

    }

    @Override
    public void onResume()
    {

    }

    @Override
    public void onExit()
    {

    }
}
