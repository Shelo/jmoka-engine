package example;

import com.moka.core.Entity;
import com.moka.physics.Collision;
import com.moka.triggers.Trigger;
import example.components.Health;

public class BulletTriggers
{
    public static class CollisionWithEnemy extends Trigger<Collision>
    {
        private static int hitDamage = 20;

        @Override
        public Object onTrigger()
        {
            entity().destroy();

            Collision collision = meta();
            Entity entity = collision.getEntity();

            Health health = entity.getComponent(Health.class);
            if (health != null)
            {
                health.takeDamage(hitDamage);
            }

            return null;
        }
    }
}
