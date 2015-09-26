package example.scenes;

import com.moka.scene.Scene;
import example.R;

public class MainScene extends Scene
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);
        R.prefabs.player.newEntity("Player");

        R.prefabs.space01.newEntity("Space01_01");

        R.prefabs.enemy01.newEntity("Enemy01_01", 532, R.screen.height / 3 * 2);
        R.prefabs.enemy02.newEntity("Enemy02_01", 500, R.screen.height / 2);
        R.prefabs.enemy03.newEntity("Enemy03_01", 532, R.screen.height / 3);

        R.prefabs.tiles.dirt01.newEntity(null, R.screen.width / 2, R.screen.height - 32);
        R.prefabs.tiles.dirt01.newEntity(null, R.screen.width / 2, 32)
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
