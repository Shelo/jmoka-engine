package example;

import com.moka.core.entity.Entity;
import com.moka.physics.Collision;
import com.moka.triggers.Trigger;
import example.components.Health;

public class BulletTriggers
{
    public static class CollisionWithEnemy extends Trigger<Collision>
    {
        private static final int HIT_DAMAGE = 20;

        @Override
        public Object onTrigger()
        {
            Collision collision = meta();
            Entity enemy = collision.getEntity();

            if (!enemy.belongsTo("Enemies"))
            {
                return null;
            }

            Health health = enemy.getComponent(Health.class);
            if (health != null)
            {
                health.takeDamage(HIT_DAMAGE);
            }

            getEntity().destroy();

            return null;
        }
    }
}
