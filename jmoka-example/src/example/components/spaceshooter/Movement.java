package example.components.spaceshooter;

import com.moka.core.ComponentAttribute;
import com.moka.core.Moka;
import com.moka.core.entity.Component;
import com.moka.math.Vector2;
import com.moka.time.TimeOut;
import com.moka.triggers.Trigger;
import com.moka.utils.Pools;
import example.Res;

public class Movement extends Component
{
    private float speed = 100;
    private TimeOut currentTimeOut;

    private Trigger switchTextureBack = new Trigger()
    {
        @Override
        public Object onTrigger()
        {
            getEntity().getSprite().setTexture(Res.textures.player);

            return null;
        }
    };

    @Override
    public void onUpdate()
    {
        float x = Moka.getInput().getAxes(Res.axes.HORIZONTAL);
        float y = Moka.getInput().getAxes(Res.axes.VERTICAL);

        Vector2 distance = Pools.vec2.take(x, y).nor().mul(speed * Moka.getTime().getDelta());
        getTransform().move(distance);
        Pools.vec2.put(distance);

        if (Moka.getInput().getButtonDown(Res.buttons.FIRE_1))
        {
            getEntity().getSprite().setTexture(Res.textures.playerShooting);

            if (currentTimeOut != null)
            {
                currentTimeOut.cancel();
            }

            currentTimeOut = Moka.getTime().newTimeOut(this, 0.1f, switchTextureBack);
        }
    }

    @Override
    public void onDestroy()
    {
        if (currentTimeOut != null)
        {
            currentTimeOut.cancel();
        }
    }

    @ComponentAttribute("Speed")
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
}
