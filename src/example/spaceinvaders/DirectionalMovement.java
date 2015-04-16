package example.spaceinvaders;

import com.moka.core.Component;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2f;
import org.lwjgl.glfw.GLFW;

public class DirectionalMovement extends Component
{
    private boolean ignoreRotation = false;
    private Directions directions;
    private float rotationSpeed = 0.1f;
    private Vector2f buffer = new Vector2f();
    private float speed = 400;

    private enum Directions {
        RIGHT,
        LEFT,
        DOWN,
        UP
    }

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
        float delta = (float) getTime().getDelta();
        getTransform().move(direction.x * delta * speed, direction.y * delta * speed);

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

    @XmlAttribute("directions")
    public void setDirections(Directions directions)
    {
        this.directions = directions;
    }
}
