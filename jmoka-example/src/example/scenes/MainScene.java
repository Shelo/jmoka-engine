package example.scenes;

import com.moka.scene.Scene;
import com.shelodev.oping2.structure.Branch;
import example.R;

public class MainScene extends Scene
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);
        R.prefabs.player.newEntity("Player");

        R.prefabs.space01.newEntity("Space01_01");

        R.prefabs.enemy01.newEntity("Enemy01_01", 532, R.screen.HEIGHT / 3 * 2);
        R.prefabs.enemy02.newEntity("Enemy02_01", 500, R.screen.HEIGHT / 2);
        R.prefabs.enemy03.newEntity("Enemy03_01", 532, R.screen.HEIGHT / 3);

        R.prefabs.tiles.dirt01.newEntity(null, R.screen.WIDTH / 2, R.screen.HEIGHT - 32);
        R.prefabs.tiles.dirt01.newEntity(null, R.screen.WIDTH / 2, 32)
                .getTransform().setRotation((float) Math.toRadians(180f));

        // Testing the DataConfigFile "user".
        System.out.println(R.save.user.getSingleString("Name"));

        int timesPlayed = R.save.user.getSingleInt("TimesPlayed");
        System.out.println("TimesPlayed: " + timesPlayed);

        R.save.user.getLeaf("TimesPlayed").setValue(0, timesPlayed + 1);

        R.save.user.save();
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
