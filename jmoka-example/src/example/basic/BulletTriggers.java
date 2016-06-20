package example.basic;

import com.moka.components.Bullet;
import com.moka.physics.Collision;
import com.moka.triggers.Trigger;
import example.basic.spaceshooter.Health;

public class BulletTriggers
{
    public static class BulletCollisionTrigger extends Trigger<Collision>
    {
        @Override
        public Object onTrigger()
        {
            Bullet bullet = getEntity().getComponent(Bullet.class);

            String group = meta().getOther().getGroup();
            if (group != null && !group.equals("Player")) {
                Health health = meta().getOther().getComponent(Health.class);

                if (health != null) {
                    health.takeDamage(bullet.getDamage());
                }
            }

            R.prefabs.explosion02.newEntity(null, getEntity().getTransform().getPosition());
            getEntity().destroy();

            return null;
        }
    }

    public static class TestTrigger extends Trigger<Collision>
    {
        @Override
        public Object onTrigger()
        {
            log(meta().getOther().getName());
            return null;
        }
    }
}
