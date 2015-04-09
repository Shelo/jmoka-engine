package example;

import com.moka.core.Application;
import com.moka.core.game.XmlContext;

public class Game
{
    private static final String SECONDARY_PATH = "example.components";

    public static void main(String[] args)
    {
        Application application = new Application();

        // set the game context for the application.
        application.setContext(new XmlContext("res/scene/scene.xml", "res/values.xml"));

        // set context options.
        application.getContext().setSecondaryPackage(SECONDARY_PATH);

        // set display options.
        application.getDisplay().createDisplay("screen_width", "screen_height", "JMoka Engine");

        // set renderer options.
        application.getRenderer().setClearColor(1, 0, 0);

        // create and start the application.
        application.create().start(60);
    }
}
