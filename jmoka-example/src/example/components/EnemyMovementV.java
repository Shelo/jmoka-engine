package example.components;

import com.moka.core.entity.Component;
import com.moka.math.Vector2;
import com.moka.utils.Pools;

public class EnemyMovementV extends Component
{
    private static final float VERTICAL_SPEED = 50;

    @Override
    public void onCreate()
    {

    }

    @Override
    public void onUpdate()
    {
        float dx = (getDisplay().getWidth() / 2 - getTransform().getPosition().x) / getDisplay().getWidth() * 2;
        float dy = - 1;

        Vector2 direction = Pools.vec2.take(dx, dy).nor();

        // set rotation with the normalized vector.
        getTransform().setRotation(direction.angle());

        // move the plane.
        direction.mul(VERTICAL_SPEED * getTime().getDelta());
        getTransform().move(direction);

        Pools.vec2.put(direction);
    }
}
