package example.scenes;

import com.moka.core.Scene;
import com.moka.core.entity.Entity;
import com.moka.math.Rectangle;
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

        Entity tileTop
                = Res.prefabs.tileDirt01.newEntity(null, Res.integers.screenWidth / 2, Res.integers.screenHeight - 32);
        tileTop.getSprite().setClipRect(0, 0, 13, 1);

        Entity tileBottom
                = Res.prefabs.tileDirt01.newEntity(null, Res.integers.screenWidth / 2, 32);
        tileBottom.getTransform().setRotation((float) Math.toRadians(180f));
        tileBottom.getSprite().setClipRect(0, 0, 13, 1);
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
