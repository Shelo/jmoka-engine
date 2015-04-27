package example.components;

import com.moka.core.entity.Component;
import com.moka.core.Input;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.MathUtil;
import com.moka.math.Vector2;
import com.moka.triggers.Trigger;

public class DirectionalMovement extends Component
{
    private static final float ANGLE_CLAMP = (float) Math.PI / 6;

    private boolean ignoreRotation = false;
    private Directions lockDirection = Directions.NONE;
    private float rotationSpeed = 0.1f;
    private Vector2 buffer = new Vector2();
    private float speed = 400;
    private Trigger<Float> angleTrigger;

    private enum Directions {
        NONE,
        VERTICAL,
        HORIZONTAL,
    }

    @Override
    public void onUpdate()
    {
        buffer.set(0, 0);

        if (lockDirection == Directions.NONE || lockDirection == Directions.HORIZONTAL)
        {
            if (getInput().getKey(Input.KEY_A))
            {
                buffer.add(Vector2.LEFT);
            }

            if (getInput().getKey(Input.KEY_D))
            {
                buffer.add(Vector2.RIGHT);
            }
        }

        if (lockDirection == Directions.NONE || lockDirection == Directions.VERTICAL)
        {
            if (lockDirection != Directions.HORIZONTAL && getInput().getKey(Input.KEY_W))
            {
                buffer.add(Vector2.UP);
            }

            if (lockDirection != Directions.HORIZONTAL && getInput().getKey(Input.KEY_S))
            {
                buffer.add(Vector2.DOWN);
            }
        }

        if (!buffer.equals(Vector2.ZERO))
        {
            buffer.nor();
            move(buffer);
        }
    }

    public void move(Vector2 direction)
    {
        float delta = (float) getTime().getDelta();
        getTransform().move(direction.x * delta * speed, direction.y * delta * speed);

        if (!ignoreRotation)
        {
            float currentAngle = getTransform().getLookAngle();
            float targetAngle = direction.angle();

            if (angleTrigger != null)
            {
                targetAngle = (float) angleTrigger.trigger(this, targetAngle);
            }

            float rotation = MathUtil.minAngle(currentAngle, targetAngle) * rotationSpeed;
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

    @XmlAttribute("lockDirection")
    public void setLockDirection(Directions lockDirection)
    {
        this.lockDirection = lockDirection;
    }

    @XmlAttribute("angleTrigger")
    public void setAngleTrigger(Trigger<Float> angleTrigger)
    {
        this.angleTrigger = angleTrigger;
    }
}
