package example;

import com.moka.triggers.Trigger;

public class EnemyTriggers
{
    public static class OnDeathEnemy extends Trigger<Object>
    {
        @Override
        public Object onTrigger()
        {
            getEntity().destroy();
            return null;
        }
    }
}
