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

            if (!meta().getEntity().getGroup().equals("Player"))
            {
                Health health = meta().getEntity().getComponent(Health.class);

                if (health != null)
                {
                    health.takeDamage(bullet.getDamage());
                }

                getEntity().destroy();
            }

            return null;
        }
    }
}
