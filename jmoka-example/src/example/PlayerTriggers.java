package example;

import com.moka.core.Prefab;
import com.moka.math.Vector2;
import com.moka.triggers.Trigger;

public class PlayerTriggers
{
    public static class FireTrigger extends Trigger<Prefab>
    {
        @Override
        public Object onTrigger()
        {
            Prefab prefab = getMeta();
            prefab.setRotation((float) Math.toRadians(90));

            Vector2 playerPos = getTransform().getPosition();

            prefab.setPosition(playerPos.x, playerPos.y + 35);
            prefab.newEntity(null);

            return null;
        }
    };
}
