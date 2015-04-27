package example.components;

import com.moka.core.entity.Component;

public class DestroyOnLeave extends Component
{
    @Override
    public void onUpdate()
    {
        float height = getApplication().getDisplay().getHeight();

        if (getTransform().getPosition().y > height)
        {
            entity().destroy();
        }
    }
}
