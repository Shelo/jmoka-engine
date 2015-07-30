package example;

import com.moka.components.Bullet;
import com.moka.physics.Collision;
import com.moka.triggers.Trigger;
import example.components.spaceshooter.Health;

public class BulletTriggers
{
    public static class BulletCollisionTrigger extends Trigger<Collision>
    {
        @Override
        public Object onTrigger()
        {
            Bullet bullet = getEntity().getComponent(Bullet.class);

            if (!meta().getOther().getGroup().equals("Player"))
            {
                Health health = meta().getOther().getComponent(Health.class);

                if (health != null)
                {
                    health.takeDamage(bullet.getDamage());
                }

                Res.prefabs.explosion02.newEntity(null, getEntity().getTransform().getPosition());
                getEntity().destroy();
            }

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
