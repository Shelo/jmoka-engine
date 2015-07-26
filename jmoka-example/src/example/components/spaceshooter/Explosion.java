package example.components.spaceshooter;

import com.moka.core.Moka;
import com.moka.core.entity.Component;

public class Explosion extends Component
{
    @Override
    public void onUpdate()
    {
        getTransform().rotate(0.3925f * Moka.getTime().getDelta());
        getTransform().scale(1.01f);

        getEntity().getSprite().getTint().a -= Moka.getTime().getDelta();

        if (getEntity().getSprite().getTint().a <= 0.05f)
        {
            getEntity().destroy();
        }
    }
}
