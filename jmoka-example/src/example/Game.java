package example;

import com.moka.core.Application;
import com.moka.core.Entity;
import com.moka.core.Input;
import com.moka.core.Prefab;
import com.moka.core.contexts.XmlContext;
import com.moka.math.MathUtil;
import com.moka.triggers.Trigger;

public class Game
{
    private static final String SECONDARY_PATH = "example.spaceinvaders";

    /*
     * This is an example of a trigger.
     */
    public static Trigger<Prefab> fireTrigger = new Trigger<Prefab>()
    {
        @Override
        public Object onTrigger()
        {
            Prefab prefab = getMeta();
            Entity entity = prefab.newEntity(null);
            return true;
        }
    };

    public static Trigger<Float> lockAngleTrigger = new Trigger<Float>()
    {
        @Override
        public Object onTrigger()
        {
            Float angle = getMeta();
            return MathUtil.clamp(angle, (float) - Math.PI / 6, (float) Math.PI / 6);
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

        application.getInput()
                .bindMouse("fire", Input.MOUSE_BUTTON_1);

        // set display options.
        application.getDisplay().createDisplay("screen_width", "screen_height", "JMoka Engine");

        // set renderer options.
        application.getRenderer().setClearColor(0, 0, 0);

        // create and start the application.
        application.create().start(60);
    }
}
