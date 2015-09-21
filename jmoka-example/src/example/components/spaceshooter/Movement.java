package example.components.spaceshooter;

import com.moka.components.RigidBody;
import com.moka.core.ComponentAttribute;
import com.moka.core.Moka;
import com.moka.core.entity.Component;
import com.moka.math.Vector2;
import com.moka.utils.Pools;
import example.R;

public class Movement extends Component
{
    private float speed = 100;
    private RigidBody body;

    @Override
    public void onCreate()
    {
        body = getComponent(RigidBody.class);
    }

    @Override
    public void onUpdate()
    {
        float x = Moka.getInput().getAxes(R.axes.HORIZONTAL);
        float y = Moka.getInput().getAxes(R.axes.VERTICAL);

        Vector2 distance = Pools.vec2.take(x, y).nor().mul(speed * Moka.getTime().getDelta());
        body.setLinearVelocity(distance.x, distance.y);
        Pools.vec2.put(distance);
    }

    @ComponentAttribute("Speed")
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
}
