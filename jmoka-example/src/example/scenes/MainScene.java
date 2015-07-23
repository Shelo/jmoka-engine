package example.scenes;

import com.moka.core.Moka;
import com.moka.core.Scene;
import example.Resources;

public class MainScene extends Scene
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);
        Resources.prefabs.player.newEntity("Player");
        Moka.getRenderer().setClearColor(1, 1, 1);
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
