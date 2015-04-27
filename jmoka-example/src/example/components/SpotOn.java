package example.components;

import com.moka.core.entity.Component;
import com.moka.core.entity.Entity;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2;

public class SpotOn extends Component
{
    private float factor = .1f;
    private Entity target;

    private Vector2 buffer = new Vector2();
    private Vector2 halfScreenSize;

    @Override
    public void onCreate()
    {
        halfScreenSize = new Vector2(getDisplay().getWidth(), getDisplay().getHeight()).mul(0.5f);
    }

    @Override
    public void onUpdate()
    {
        Vector2 position = getTransform().getPosition();
        Vector2 targetPos = target.getTransform().getPosition();
        buffer.set(targetPos).sub(halfScreenSize);
        position.add(buffer.sub(position).mul(factor));
    }

    @XmlAttribute(value = "target", required = true)
    public void setTarget(Entity target)
    {
        this.target = target;
    }

    @XmlAttribute("factor")
    public void setFactor(float factor)
    {
        this.factor = factor;
    }
}
