package example.scenes;

import com.moka.core.Scene;
import example.Res;

public class MainScene extends Scene
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);
        Res.prefabs.player.newEntity("Player");

        Res.prefabs.enemy01.newEntity("Enemy01_01", 532, Res.integers.screenHeight / 3 * 2);
        Res.prefabs.enemy02.newEntity("Enemy02_01", 500, Res.integers.screenHeight / 2);
        Res.prefabs.enemy03.newEntity("Enemy03_01", 532, Res.integers.screenHeight / 3);

        for (int i = 0; i < 13; i++)
        {
            Res.prefabs.tileDirt01.newEntity(null, 32 + i * 64, Res.integers.screenHeight - 32);
        }

        Res.prefabs.tileDirt01.setRotation((float) Math.toRadians(180));
        for (int i = 0; i < 13; i++)
        {
            Res.prefabs.tileDirt01.newEntity(null, 32 + i * 64, 32);
        }
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
