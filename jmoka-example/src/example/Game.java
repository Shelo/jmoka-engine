package example;

import com.moka.core.Application;
import com.moka.input.Input;
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
        Application app = new Application(new Resources("jmoka-example/assets/"));

        // set some inputs.
        Moka.getInput().bindKey(Resources.buttons.FIRE_1, Input.KEY_Z);
        Moka.getInput().bindKey(Resources.buttons.FIRE_2, Input.KEY_X);
        Moka.getInput().bindAxes(Resources.axes.HORIZONTAL, Input.KEY_LEFT, Input.KEY_RIGHT);
        Moka.getInput().bindAxes(Resources.axes.VERTICAL, Input.KEY_DOWN, Input.KEY_UP);

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
