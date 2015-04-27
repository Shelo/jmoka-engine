package example.components;

import com.moka.core.entity.Component;
import com.moka.core.entity.Entity;
import com.moka.core.time.Timer;
import com.moka.math.Vector2;
import com.moka.triggers.Trigger;
import com.moka.utils.None;
import com.moka.utils.Pools;

import java.util.List;

public class EnemyMovementController extends Component
{
    private static final int MOVE_DISTANCE = 64;
    private boolean toRight = true;

    private List<Entity> enemies;
    private Vector2 screenSize;

    @Override
    public void onCreate()
    {
        enemies = entity().getContext().getEntitiesFromGroup("Enemies");
        screenSize = getApplication().getDisplay().getSizeVector();

        getTime().newTimer(this, 0.5f, enemyMoveTrigger);
    }

    public boolean move()
    {
        Vector2 mostLeft = enemies.get(0).getTransform().getPosition();
        Vector2 mostRight = enemies.get(0).getTransform().getPosition();

        for (Entity enemy : enemies)
        {
            if (enemy.getTransform().getPosition().x > mostRight.x)
            {
                mostRight = enemy.getTransform().getPosition();
            }

            if (enemy.getTransform().getPosition().x < mostLeft.x)
            {
                mostLeft = enemy.getTransform().getPosition();
            }
        }

        Vector2 distance = Pools.vec2.take(0, 0);
        distance.x = toRight ? MOVE_DISTANCE : MOVE_DISTANCE * (- 1);

        if ((toRight && mostRight.x + MOVE_DISTANCE > screenSize.x) || (!toRight && mostLeft.x - MOVE_DISTANCE < 0))
        {
            distance.y = - MOVE_DISTANCE;
            distance.x = 0;
            toRight = ! toRight;
        }

        for (Entity enemy : enemies)
        {
            enemy.getTransform().move(distance);
        }

        return false;
    }

    private Trigger<None> enemyMoveTrigger = new Trigger<None>()
    {
        @Override
        public Object onTrigger()
        {
            return move();
        }
    };
}
