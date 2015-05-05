package example.components;

import com.moka.core.entity.Component;
import com.moka.core.entity.Entity;
import com.moka.core.readers.ComponentAttribute;
import com.moka.math.Vector2;

public class SpotOn extends Component
{
    private Float bottom;
    private Float right;
    private Float left;
    private Float top;

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

        // bottom bound.
        position.y = checkBound(position.y, bottom, true);
        position.y = checkBound(position.y, top, false);
        position.x = checkBound(position.x, left, true);
        position.x = checkBound(position.x, right, false);
    }

    public float checkBound(float original, Float value, boolean lower)
    {
        if (value == null)
        {
            return original;
        }

        return lower ? (original < value ? value : original) : (original > value ? value : original);
    }

    @ComponentAttribute("left")
    public void setLeft(Float left)
    {
        this.left = left;
    }

    @ComponentAttribute("right")
    public void setRight(Float right)
    {
        this.right = right;
    }

    @ComponentAttribute("bottom")
    public void setBottom(Float bottom)
    {
        this.bottom = bottom;
    }

    @ComponentAttribute("top")
    public void setTop(Float top)
    {
        this.top = top;
    }

    @ComponentAttribute(value = "target", required = true)
    public void setTarget(Entity target)
    {
        this.target = target;
    }

    @ComponentAttribute("factor")
    public void setFactor(float factor)
    {
        this.factor = factor;
    }
}
