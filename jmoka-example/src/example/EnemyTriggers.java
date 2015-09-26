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

    public static class OnEnterArea extends Trigger<Entity>
    {
        @Override
        public Object onTrigger()
        {
            System.out.println(1);
            return null;
        }
    }
}
