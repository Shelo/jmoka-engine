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

        Res.prefabs.space01.newEntity("Space01_01");

        Res.prefabs.enemy01.newEntity("Enemy01_01", 532, Res.screen.height / 3 * 2);
        Res.prefabs.enemy02.newEntity("Enemy02_01", 500, Res.screen.height / 2);
        Res.prefabs.enemy03.newEntity("Enemy03_01", 532, Res.screen.height / 3);

        Res.prefabs.tiles.dirt01.newEntity(null, Res.screen.width / 2, Res.screen.height - 32);
        Res.prefabs.tiles.dirt01.newEntity(null, Res.screen.width / 2, 32)
                .getTransform().setRotation((float) Math.toRadians(180f));
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
