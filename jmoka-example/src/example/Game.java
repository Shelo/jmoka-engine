package example;

import com.moka.core.Application;
import com.moka.core.Input;
import com.moka.core.Moka;
import example.scenes.MainScene;
import example.scenes.SecondScene;

public class Game
{
    /**
     * Main entrance of the engine.
     */
    public static void main(String[] args)
    {
        Application app = new Application(new Resources());

        // set some inputs.
        Moka.getInput().bindKey(Resources.buttons.FIRE_1, Input.KEY_SPACE);
        Moka.getInput().bindKey(Resources.buttons.FIRE_2, Input.KEY_M);
        Moka.getInput().bindKey(Resources.buttons.LEFT, Input.KEY_LEFT);
        Moka.getInput().bindKey(Resources.buttons.UP, Input.KEY_UP);
        Moka.getInput().bindKey(Resources.buttons.RIGHT, Input.KEY_RIGHT);
        Moka.getInput().bindKey(Resources.buttons.DOWN, Input.KEY_DOWN);

        // set display options.
        Moka.getDisplay().createDisplay(800, 450, "JMoka Engine");

        // set renderer options.
        Moka.getRenderer().setClearColor(0, 0, 0);

        // set up packages.
        Moka.getNameManager().usePackage("SpaceShooter", "example.components.spaceshooter");

        // set up scenes.
        Moka.getContext().addScene(new MainScene());
        Moka.getContext().addScene(new SecondScene());

        Moka.getContext().setMainScene(MainScene.class);

        // create and start the application.
        app.create().start(60);
    }
}
