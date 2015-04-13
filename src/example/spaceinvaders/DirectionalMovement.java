package example.spaceinvaders;

import com.moka.core.Component;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2f;
import org.lwjgl.glfw.GLFW;

public class DirectionalMovement extends Component
{
    private boolean ignoreRotation = false;
    private float rotationSpeed = 0.1f;
    private Vector2f buffer = new Vector2f();
    private float speed = 400;

    @Override
    public void onUpdate()
    {
        buffer.set(0, 0);

        if (getInput().getKey(GLFW.GLFW_KEY_A))
        {
            buffer.add(Vector2f.LEFT);
        }

        if (getInput().getKey(GLFW.GLFW_KEY_D))
        {
            buffer.add(Vector2f.RIGHT);
        }

        if (getInput().getKey(GLFW.GLFW_KEY_W))
        {
            buffer.add(Vector2f.UP);
        }

        if (getInput().getKey(GLFW.GLFW_KEY_S))
        {
            buffer.add(Vector2f.DOWN);
        }

        if (!buffer.equals(Vector2f.ZERO))
        {
            buffer.nor();
            move(buffer);
        }
    }

    public void move(Vector2f direction)
    {
        getTransform().move(direction.x * (float) getTime().getDelta() * speed,
                direction.y * (float) getTime().getDelta() * speed);

        if (!ignoreRotation)
        {
            float currentAngle = getTransform().getLookAngle();
            float rotation = (direction.angle() - currentAngle) * rotationSpeed;
            getTransform().rotate(rotation);
        }
    }

    @XmlAttribute("ignoreRotation")
    public void setIgnoreRotation(boolean ignoreRotation)
    {
        this.ignoreRotation = ignoreRotation;
    }

    @XmlAttribute("speed")
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }

    @XmlAttribute("rotationSpeed")
    public void setRotationSpeed(float rotationSpeed)
    {
        this.rotationSpeed = rotationSpeed;
    }
}
