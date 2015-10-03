package example;

import com.moka.core.Application;
import com.moka.input.Input;
import com.moka.core.Moka;
import example.scenes.HotlineMiamiClone;
import example.scenes.MainScene;

public class Game
{
    /**
     * Main entrance of the engine.
     */
    public static void main(String[] args)
    {
        Application app = new Application(new R("jmoka-example/assets/"));

        // set some inputs.
        Moka.getInput().bindKey(R.buttons.FIRE_1, Input.KEY_Z);
        Moka.getInput().bindKey(R.buttons.FIRE_2, Input.KEY_X);
        Moka.getInput().bindAxes(R.axes.HORIZONTAL, Input.KEY_A, Input.KEY_D);
        Moka.getInput().bindAxes(R.axes.VERTICAL, Input.KEY_S, Input.KEY_W);

        // set display options.
        Moka.getDisplay().createDisplay(R.screen.WIDTH, R.screen.HEIGHT, "JMoka Engine");

        // set renderer options.
        Moka.getRenderer().setClearColor(0, 0, 0);

        // set up packages.
        Moka.getNameManager().usePackage("SpaceShooter", "jmoka-example/src/",
                "example.components.spaceshooter", true);
        Moka.getNameManager().usePackage("Hotline", "jmoka-example/src/", "example.components.hotline", true);

        // set up scenes.
        Moka.getContext().addScene(new HotlineMiamiClone());
        Moka.getContext().addScene(new MainScene());
        Moka.getContext().setMainScene(HotlineMiamiClone.class);

        // create and start the application at 60 fps.
        app.create().start(60);
    }
}
