package example.components;

import com.moka.core.Input;
import com.moka.core.entity.Component;
import com.moka.core.readers.ComponentAttribute;
import com.moka.physics.Collision;
import com.moka.triggers.Trigger;

public class PlatformerController extends Component
{
    private float jumpSpeed = 10f;
    private float gravity = -19.8f;
    private float fallingSpeed;

    private Trigger<Collision> collisionTrigger = new Trigger<Collision>()
    {
        @Override
        public Object onTrigger()
        {
            if (meta().getDirection().y > 0 && fallingSpeed <= 0 && meta().getEntity().belongsTo("Floor"))
            {
                fallingSpeed = 0;
            }

            if (meta().getDirection().y < 0 && meta().getEntity().belongsTo("Floor"))
            {
                fallingSpeed = 0;
            }

            return null;
        }
    };

    @Override
    public void onCreate()
    {
        getCollider().setCollisionTrigger(collisionTrigger);
    }

    @Override
    public void onUpdate()
    {
        if (fallingSpeed == 0 && getInput().getKeyDown(Input.KEY_SPACE))
        {
            fallingSpeed = jumpSpeed;
        }

        fallingSpeed += gravity * getTime().getDelta();
        getTransform().moveY(fallingSpeed);
    }

    @ComponentAttribute("gravity")
    public void setGravity(float gravity)
    {
        this.gravity = gravity;
    }
}
