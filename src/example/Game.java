package example;

import com.moka.core.Application;
import com.moka.core.Prefab;
import com.moka.core.contexts.XmlContext;
import com.moka.core.triggers.Trigger;

public class Game
{
    private static final String SECONDARY_PATH = "example.spaceinvaders";

    /*
     * This is an example of a trigger.
     */
    public static Trigger<Prefab> fireTrigger = new Trigger<Prefab>()
    {
        @Override
        public boolean onTrigger()
        {
            Prefab prefab = getMeta();
            prefab.newEntity(null);
            return true;
        }
    };

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
        application.getRenderer().setClearColor(0, 0, 0);

        // create and start the application.
        application.create().start(60);
    }
}
