package example.components;

import com.moka.core.Component;
import com.moka.core.time.Timer;
import com.moka.math.Vector2;
import com.moka.triggers.Trigger;
import com.moka.utils.None;

public class EnemyAI extends Component
{
    private static final int MOVE_DISTANCE = 64;
    private static boolean toRight = true;
    private Timer<None> timer;
    private Vector2 screenSize;

    @Override
    public void onCreate()
    {
        timer = getTime().newTimer(this, 0.5f, move);
        screenSize = getApplication().getDisplay().getSizeVector();
    }

    @Override
    public void onUpdate()
    {

    }

    public Trigger<None> move = new Trigger<None>()
    {
        @Override
        public Object onTrigger()
        {
            float distance = MOVE_DISTANCE;

            if (!toRight)
            {
                distance *= -1;
            }

            if (distance + transform().getPosition().x > screenSize.x)
            {
                toRight = false;
            }

            getTransform().moveX(distance);
            return false;
        }
    };
}
