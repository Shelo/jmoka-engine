package example;

import com.moka.core.Application;
import com.moka.core.Input;
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
        app.getInput().bindKey("fire", Input.KEY_SPACE);

        // set display options.
        app.getDisplay().createDisplay(800, 450, "JMoka Engine");

        // set renderer options.
        app.getRenderer().setClearColor(0, 0, 0);

        // set up scenes.
        app.getContext().addScene(new MainScene());
        app.getContext().addScene(new SecondScene());

        app.getContext().setMainScene(MainScene.class);

        // create and start the application.
        app.create().start(60);
    }
}
