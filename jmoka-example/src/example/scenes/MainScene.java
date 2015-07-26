package example.scenes;

import com.moka.core.Moka;
import com.moka.core.Scene;
import com.moka.core.entity.Entity;
import com.moka.math.Rectangle;
import example.Res;

import java.util.List;

public class MainScene extends Scene
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);
        Res.prefabs.player.newEntity("Player");

        Res.prefabs.space01.newEntity("Space01_01");

        Res.prefabs.enemy01.newEntity("Enemy01_01", 532, Res.integers.screenHeight / 3 * 2);
        Res.prefabs.enemy02.newEntity("Enemy02_01", 500, Res.integers.screenHeight / 2);
        Res.prefabs.enemy03.newEntity("Enemy03_01", 532, Res.integers.screenHeight / 3);

        Res.prefabs.tileDirt01.newEntity(null, Res.integers.screenWidth / 2, Res.integers.screenHeight - 32);
        Res.prefabs.tileDirt01.newEntity(null, Res.integers.screenWidth / 2, 32)
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
