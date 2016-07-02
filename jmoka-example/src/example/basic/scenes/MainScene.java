package example.basic.scenes;

import com.moka.scene.Scene;
import com.moka.scene.entity.Entity;
import example.basic.R;
import example.basic.spaceshooter.Background;
import example.basic.spaceshooter.CameraFollow;

public class MainScene extends Scene
{
    @Override
    public void onCreate()
    {
        Entity camera = newCamera("MainCamera", true);

        Entity player = R.prefabs.player.newEntity("Player");
        player.getComponent(CameraFollow.class).setCamera(camera.getTransform());

        // create the space.
        Entity background = R.prefabs.background.newEntity("Background");
        background.getComponent(Background.class).setPlayer(player.getTransform());

        // R.prefabs.enemy01.newEntity("Enemy01_01", 532, R.screen.HEIGHT / 3 * 2);
        // R.prefabs.enemy02.newEntity("Enemy02_01", 500, R.screen.HEIGHT / 2);
        // R.prefabs.enemy03.newEntity("Enemy03_01", 532, R.screen.HEIGHT / 3);
    }

    @Override
    public void onPostCreate()
    {

    }

    @Override
    public void onUpdate()
    {

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
