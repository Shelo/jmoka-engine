package example;

import com.moka.core.Application;
import com.moka.core.Prefab;
import com.moka.core.contexts.XmlContext;
import com.moka.core.triggers.Trigger;
import com.moka.core.triggers.TriggerEvent;
import com.moka.math.Vector2f;

public class Game
{
    private static final String SECONDARY_PATH = "example.spaceinvaders";

    /*
     * This is an example of a trigger.
     */
    public static Trigger<Prefab> fireTrigger = new Trigger<Prefab>()
    {
        private int counter = 0;

        @Override
        public boolean onTrigger(TriggerEvent<Prefab> event)
        {
            Prefab prefab = event.getMeta();
            prefab.setPosition(event.getEntity().getTransform().getPosition());
            prefab.newEntity("Bullet-" + (counter++));
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
