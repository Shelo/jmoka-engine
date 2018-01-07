package example.integration.integration;

import com.moka.core.Moka;
import com.moka.math.Vector2;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.utils.Pools;

public class LookAtMouse extends Component
{
    private float speed;
    private Vector2 cursorBuffer;

    @Override
    public void onCreate()
    {
        cursorBuffer = Pools.vec2.take();
    }

    @Override
    public void onUpdate()
    {
        Moka.getInput().getCursorWorldPos(cursorBuffer);

        cursorBuffer.sub(getTransform().getPosition());

        getEntity().getTransform().setRotationRadians(cursorBuffer.angle());
    }

    @Override
    public void onDestroy()
    {
        Pools.vec2.put(cursorBuffer);
    }

    @ComponentAttribute(value = "Speed", required = true)
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
}
