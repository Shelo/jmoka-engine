package example.components;

import com.moka.core.Component;
import com.moka.math.Vector2;

public class DestroyOnLeave extends Component
{
    @Override
    public void onUpdate()
    {
        float height = getApplication().getDisplay().getHeight();

        if (getTransform().getPosition().y > height)
        {
            getEntity().destroy();
        }
    }
}
