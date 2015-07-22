package example.scenes;

import com.moka.core.Scene;
import com.moka.core.entity.Entity;
import example.Resources;

public class SecondScene extends Scene
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);
        Entity entity = newEntity("Player", Resources.textures.player, 0);

        entity.getTransform().move(200, 225);
    }

    @Override
    public void onLeave()
    {

    }

    @Override
    public void onResume()
    {

    }

    @Override
    public void onExit()
    {

    }
}
