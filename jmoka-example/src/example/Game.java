package example;

import com.moka.core.Application;
import com.moka.input.Input;
import com.moka.core.Moka;
import example.scenes.BlockyScene;

public class Game
{
    /**
     * Main entrance of the engine.
     */
    public static void main(String[] args)
    {
        Application app = new Application(new Res("jmoka-example/assets/"));

        // set some inputs.
        Moka.getInput().bindKey(Res.buttons.FIRE_1, Input.KEY_Z);
        Moka.getInput().bindKey(Res.buttons.FIRE_2, Input.KEY_X);
        Moka.getInput().bindAxes(Res.axes.HORIZONTAL, Input.KEY_LEFT, Input.KEY_RIGHT);
        Moka.getInput().bindAxes(Res.axes.VERTICAL, Input.KEY_DOWN, Input.KEY_UP);

        // set display options.
        Moka.getDisplay().createDisplay(Res.screen.width, Res.screen.height, "JMoka Engine");

        // set renderer options.
        Moka.getRenderer().setClearColor(0, 0, 0);

        // set up packages.
        Moka.getNameManager().usePackage("example.components.spaceshooter");

        // set up scenes.
        Moka.getContext().addScene(new BlockyScene());

        Moka.getContext().setMainScene(BlockyScene.class);

        Moka.getPhysics().setGravity(0, -9.8f);

        // create and start the application.
        app.create().start(60);
    }
}
