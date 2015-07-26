package example.components.spaceshooter;

import com.moka.core.Moka;
import com.moka.core.entity.Component;
import com.moka.time.TimeOut;
import com.moka.triggers.Trigger;
import example.Res;

public class SimpleShooter extends Component
{
    private TimeOut currentTimeOut;
    private float bulletOffsetX;
    private float bulletOffsetY;

    private static int i;

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
    public void onCreate()
    {
        bulletOffsetX = Res.textures.player.getWidth() / 2;
        bulletOffsetY = 20;
    }

    @Override
    public void onUpdate()
    {
        if (Moka.getInput().getButtonDown(Res.buttons.FIRE_1))
        {
            getEntity().getSprite().setTexture(Res.textures.playerShooting);

            float x = getTransform().getPosition().x + bulletOffsetX;

            Res.prefabs.bullet01.newEntity("B" + (i++), x, getTransform().getPosition().y + bulletOffsetY);
            Res.prefabs.bullet01.newEntity("B" + (i++), x, getTransform().getPosition().y - bulletOffsetY);

            // switch the texture.
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
}
