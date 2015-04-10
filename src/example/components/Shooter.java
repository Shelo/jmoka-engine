package example.components;

import com.moka.components.Bullet;
import com.moka.components.Component;
import com.moka.core.Entity;
import com.moka.core.Prefab;
import com.moka.core.triggers.Trigger;
import com.moka.core.triggers.TriggerEvent;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Shooter extends Component
{
    private Vector2f buf = new Vector2f();

    private Trigger<Vector2f> fireTrigger;
    private Prefab bullet;
    private int counter;

    @Override
    public void onUpdate()
    {
        if (getInput().getMouseDown(GLFW.GLFW_MOUSE_BUTTON_1))
        {
            buf.set(getInput().getCursorPos());
            Vector2f dir = buf.sub(getTransform().getPosition()).nor();

            bullet.setPosition(getTransform().getPosition());
            Entity entity = bullet.newEntity("Bullet" + (counter++));

            Bullet bullet1 = entity.getComponent(Bullet.class);
            bullet1.setDirection(dir);

            if (fireTrigger != null)
            {
                TriggerEvent<Vector2f> triggerEvent = new TriggerEvent<>(this, dir);
                if (!fireTrigger.onTrigger(triggerEvent))
                {
                    entity.destroy();
                }
            }
        }
    }

    @XmlAttribute("fireTrigger")
    public void setFireTrigger(Trigger<Vector2f> trigger) {
        fireTrigger = trigger;
    }

    @XmlAttribute("bulletPrefab")
    public void setBullet(Prefab bullet)
    {
        this.bullet = bullet;
    }
}
