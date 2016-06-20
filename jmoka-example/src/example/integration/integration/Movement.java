package example.integration.integration;

import com.moka.core.Moka;
import com.moka.math.Vector2;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.utils.Pools;
import example.basic.R;

public class Movement extends Component
{
    private float speed;

    @Override
    public void onUpdate()
    {
        float horizontal = Moka.getInput().getAxes(R.axes.HORIZONTAL);
        float vertical = Moka.getInput().getAxes(R.axes.VERTICAL);

        Vector2 buffer = Pools.vec2.take();
        {
            buffer.set(horizontal, vertical);
            getTransform().move(buffer.mul(getDelta() * speed));
        }
        Pools.vec2.put(buffer);
    }

    @ComponentAttribute(value = "Speed", required = true)
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
}
