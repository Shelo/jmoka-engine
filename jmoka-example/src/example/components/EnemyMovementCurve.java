package example.components;

import com.moka.core.entity.Component;
import com.moka.core.time.StopWatch;
import com.moka.math.Vector2;
import com.moka.utils.Pools;

public class EnemyMovementCurve extends Component
{
    private static final float VERTICAL_SPEED = - 50;
    private static final float AMPLITUDE = 75;

    private float initialPosition;
    private StopWatch stopWatch;
    private float phi;

    @Override
    public void onCreate()
    {
        stopWatch = getTime().newStopWatch();
        initialPosition = getTransform().getPosition().x;

        phi = (float) Math.random();
    }

    @Override
    public void onUpdate()
    {
        float curvedX = initialPosition + (float) Math.sin(stopWatch.getElapsed() + phi) * AMPLITUDE;
        float dx = curvedX - getTransform().getPosition().x;
        float dy = getTime().getDelta() * VERTICAL_SPEED;
        getTransform().move(dx, dy);

        Vector2 movement = Pools.vec2.take(dx, dy);
        movement.nor();

        getTransform().setRotation(movement.angle());
    }
}
