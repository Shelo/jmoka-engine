package example.integration.integration;

import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;

public class Rotation extends Component
{
    private float speed;

    @Override
    public void onUpdate()
    {
        getTransform().rotate(speed * getDelta());
    }

    @ComponentAttribute(value = "Speed", required = true)
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
}
