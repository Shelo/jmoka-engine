package example;

import com.moka.core.Application;
import com.moka.core.Input;
import com.moka.core.contexts.Context;

public class Game extends Context
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);
        newEntity("Player", Assets.textures.player, 0);
    }

    @Override
    public void onStop()
    {

    }

    /**
     * Main entrance of the engine.
     */
    public static void main(String[] args)
    {
        Application app = new Application(new Game(), new Assets());

        // set some inputs.
        app.getInput().bindKey("fire", Input.KEY_SPACE);

        // set display options.
        app.getDisplay().createDisplay(800, 450, "JMoka Engine");

        // set renderer options.
        app.getRenderer().setClearColor(0, 0, 0);

        // create and start the application.
        app.create().start(60);
    }
}
