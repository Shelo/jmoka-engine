package example.scenes;

import com.moka.graphics.Shader;
import com.moka.scene.Scene;
import com.moka.utils.CoreUtil;
import com.moka.utils.JMokaLog;
import com.shelodev.oping2.structure.Branch;
import com.shelodev.oping2.structure.Leaf;
import example.R;

public class MainScene extends Scene
{
    private Shader shader;

    @Override
    public void onCreate()
    {
        shader = new Shader(CoreUtil.readFile("jmoka-example/assets/shaders/test_vertex.glsl"),
                CoreUtil.readFile("jmoka-example/assets/shaders/test_fragment.glsl"));

        newCamera("MainCamera", true);
        R.prefabs.player.newEntity("Player").getSprite();

        R.prefabs.space01.newEntity("Space01_01");

        R.prefabs.enemy01.newEntity("Enemy01_01", 532, R.screen.height / 3 * 2);
        R.prefabs.enemy02.newEntity("Enemy02_01", 500, R.screen.height / 2);
        R.prefabs.enemy03.newEntity("Enemy03_01", 532, R.screen.height / 3);

        R.prefabs.tiles.dirt01.newEntity(null, R.screen.width / 2, R.screen.height - 32);
        R.prefabs.tiles.dirt01.newEntity(null, R.screen.width / 2, 32)
                .getTransform().setRotation((float) Math.toRadians(180f));

        Branch user1 = R.save.user.getRoot().getBranch(0);
        System.out.println(user1.getSingleString("Name"));

        int timesPlayed = user1.getSingleInt("TimesPlayed");
        System.out.println("TimesPlayed: " + timesPlayed);

        user1.getLeaf("TimesPlayed").setValue(0, String.valueOf(timesPlayed + 1));

        R.save.user.save();
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
