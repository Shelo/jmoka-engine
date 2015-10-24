package example.components.hotline;

import com.moka.core.Moka;
import com.moka.math.Vector2;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.Entity;

public class CameraFollow extends Component
{
    private Vector2 halfScreenSize;

    @Override
    public void onCreate()
    {
        halfScreenSize = Moka.getDisplay().getSizeVector().cpy().mul(0.5f);
    }

    @Override
    public void onUpdate()
    {
        Entity camera = Moka.getRenderer().getCamera().getEntity();
        //camera.getTransform().setPosition(getTransform().getPosition());
        //camera.getTransform().move(- halfScreenSize.x, - halfScreenSize.y);
    }
}
