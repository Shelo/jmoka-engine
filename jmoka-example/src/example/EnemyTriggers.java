package example;

import com.moka.scene.entity.Entity;
import com.moka.triggers.Trigger;

public class EnemyTriggers
{
    public static class OnDeathEnemy extends Trigger<Object>
    {
        @Override
        public Object onTrigger()
        {
            getEntity().destroy();
            R.prefabs.explosion01.newEntity(null, getEntity().getTransform().getPosition());
            return null;
        }
    }
}
