package example;

import com.moka.components.Debugger;
import com.moka.core.Application;
import com.moka.core.Input;
import com.moka.core.contexts.XmlContext;
import com.moka.core.sync.EntitySchemeWriter;
import example.components.*;

public class Game
{
    private static final String SECONDARY_PATH = "example.components";

    public static void syncXml()
    {
        EntitySchemeWriter.register(DirectionalMovement.class);
        EntitySchemeWriter.register(ShipMovement.class);
        EntitySchemeWriter.register(Debugger.class);
        EntitySchemeWriter.register(SpotOn.class);
        EntitySchemeWriter.register(DestroyOnLeave.class);
        EntitySchemeWriter.register(Health.class);
        EntitySchemeWriter.register(EnemyMovementController.class);

        EntitySchemeWriter.render();
    }

    /**
     * Main entrance of the engine.
     */
    public static void main(String[] args)
    {
        Application app = new Application();

        // set the game context for the application.
        app.setContext(new XmlContext("res/scene/scene.xml", "res/values.xml"));

        // set context options.
        app.getContext().setSecondaryPackage(SECONDARY_PATH);

        // set some inputs.
        app.getInput().bindMouse("fire", Input.MOUSE_BUTTON_1);

        InputBindings.bindWasd(app.getInput());

        // set display options.
        app.getDisplay().createDisplay("screen_width", "screen_height", "JMoka Engine");

        // set renderer options.
        app.getRenderer().setClearColor(0, 0, 0);

        app.getCore().enableExperimentalFeedback();

        // create and start the application.
        app.create().start(60);

        syncXml();
    }
}
