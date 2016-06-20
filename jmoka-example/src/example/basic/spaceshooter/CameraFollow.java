package example.basic.spaceshooter;

import com.moka.math.Vector2;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.Transform;
import example.basic.R;

public class CameraFollow extends Component
{
    private Transform camera;
    private Vector2 halfScreen;

    @Override
    public void onCreate()
    {
        halfScreen = new Vector2(R.screen.WIDTH / 2, R.screen.HEIGHT / 2);
    }

    @Override
    public void onUpdate()
    {
        camera.setPosition(getTransform().getPosition().x - halfScreen.x,
                getTransform().getPosition().y - halfScreen.y);
    }

    public void setCamera(Transform camera)
    {
        this.camera = camera;
    }
}
