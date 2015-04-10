package example;

import com.moka.components.Component;
import com.moka.core.Application;
import com.moka.core.contexts.XmlContext;
import com.moka.core.triggers.Trigger;
import com.moka.core.triggers.TriggerEvent;
import com.moka.math.Vector2f;
import com.moka.utils.JMokaException;

import java.lang.reflect.Field;

public class Game
{
    private static final String SECONDARY_PATH = "example.components";

    /**
     * Main entrance of the engine.
     */
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
        application.getRenderer().setClearColor(1, 1, 1);

        // create and start the application.
        application.create().start(60);
    }
}
