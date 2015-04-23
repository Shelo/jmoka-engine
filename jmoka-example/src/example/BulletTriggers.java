package example;

import com.moka.physics.Collision;
import com.moka.triggers.Trigger;

public class BulletTriggers
{
    public static class CollisionWithEnemy extends Trigger<Collision>
    {
        @Override
        public Object onTrigger()
        {
            System.out.println(1);
            return null;
        }
    }
}
