package example.components.spaceshooter;

import com.moka.core.ComponentAttribute;
import com.moka.core.Moka;
import com.moka.core.entity.Component;
import com.moka.math.Vector2;
import com.moka.time.TimeOut;
import com.moka.triggers.Trigger;
import com.moka.utils.Pools;
import example.Res;

public class Movement extends Component
{
    private float speed = 100;

    @Override
    public void onUpdate()
    {
        float x = Moka.getInput().getAxes(Res.axes.HORIZONTAL);
        float y = Moka.getInput().getAxes(Res.axes.VERTICAL);

        Vector2 distance = Pools.vec2.take(x, y).nor().mul(speed * Moka.getTime().getDelta());
        getTransform().move(distance);
        Pools.vec2.put(distance);
    }

    @ComponentAttribute("Speed")
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
}
