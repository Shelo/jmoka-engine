package example.basic.spaceshooter;

import com.moka.components.Sprite;
import com.moka.core.Moka;
import com.moka.scene.entity.Component;

public class Explosion extends Component
{
    @Override
    public void onUpdate()
    {
        getTransform().rotate(0.3925f * Moka.getTime().getDelta());
        getTransform().scale(1.01f);

        Sprite sprite = (Sprite) getEntity().getDrawable();
        sprite.getTint().a -= Moka.getTime().getDelta();

        if (sprite.getTint().a <= 0.05f)
            getEntity().destroy();
    }
}
