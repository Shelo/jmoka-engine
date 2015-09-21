package example.scenes.blocky;

import com.moka.components.Area;
import com.moka.components.Interval;
import com.moka.components.RigidBody;
import com.moka.components.StaticBody;
import com.moka.core.Scene;
import com.moka.core.entity.Entity;
import com.moka.physics.PhysicsBody;
import com.moka.triggers.CursorTriggers;
import com.moka.triggers.Trigger;
import example.R;

public class BlockyScene extends Scene
{
    private Entity currentEntity;

    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);

        // create the floor.
        Entity entity = newEntity("Floor", R.textures.blank, 0);
        entity.getSprite().setTint(0.5f, 0.1f, 0.1f, 1);
        StaticBody staticBody = new StaticBody();
        entity.addComponent(staticBody);
        staticBody.setSize(R.screen.width, 64);
        staticBody.setShape(PhysicsBody.Shapes.BOX);
        entity.getTransform().setSize(R.screen.width, 64);
        entity.getTransform().setPosition(R.screen.width / 2, 32);

        Entity blocky1 = R.prefabs.blocky.newEntity("Block1", 400, 96);
        Entity blocky2 = R.prefabs.blocky.newEntity("Block2", 400, 160);
        Entity blocky3 = R.prefabs.blocky.newEntity("Block3", 300, 128);
        blocky3.getComponent(RigidBody.class).setSize(64, 128);
        blocky3.getTransform().setSize(64, 128);

        Entity blocky4 = R.prefabs.blocky.newEntity("Block4", 350, 224);
        blocky4.getComponent(RigidBody.class).setSize(192, 64);
        blocky4.getTransform().setSize(192, 64);

        R.prefabs.ball.newEntity("Ball", 350, 272);

        // mouse follower.
        Entity cursor = newEntity("Cursor", 0);
        Interval setAtMouse = new Interval();
        cursor.addComponent(setAtMouse);
        cursor.addComponent(new MouseClick());
        setAtMouse.setTrigger(new CursorTriggers.MoveToCursor());
        Area area = new Area();
        area.setShape(PhysicsBody.Shapes.CIRCLE);
        area.setRadius(5);
        area.setOnEnterTrigger(new Trigger<Entity>()
        {
            @Override
            public Object onTrigger()
            {
                getEntity().getComponent(MouseClick.class).setCurrentEntity(meta());
                return null;
            }
        });
        area.setOnExitTrigger(new Trigger<Entity>()
        {
            @Override
            public Object onTrigger()
            {
                getEntity().getComponent(MouseClick.class).setCurrentEntity(null);
                return null;
            }
        });
        cursor.addComponent(area);
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
