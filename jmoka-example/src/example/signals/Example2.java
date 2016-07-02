package example.signals;

import com.moka.core.Application;
import com.moka.core.Moka;
import com.moka.input.Input;
import com.moka.scene.Scene;
import com.moka.scene.entity.Entity;

public class Example2 extends Scene
{
    @Override
    public void onCreate()
    {
        Entity camera = newCamera("Main Camera", true);
        Entity demo = R.prefabs.demo.newEntity("Demo");
    }

    @Override
    public void onPostCreate()
    {

    }

    @Override
    public void onUpdate()
    {

    }

    @Override
    public void onLeave()
    {

    }

    @Override
    public void onResume()
    {

    }

    @Override
    public void onExit()
    {

    }

    /**
     * Main entrance of the engine.
     */
    public static void main(String[] args)
    {
        Application app = new Application(new R("jmoka-example/assets/"));

        // set some inputs.
        Moka.getInput().bindButton(R.buttons.FIRE_1, Input.KEY_SPACE);

        // set display options.
        Moka.getDisplay().createDisplay(R.screen.WIDTH, R.screen.HEIGHT, "JMoka Engine");

        // set renderer options.
        Moka.getRenderer().setClearColor(0, 0, 0);

        // set up packages.
        Moka.getNameManager().usePackage("Demo", "jmoka-example/src/",
                "example2.components.demo", true);

        // set up scenes.
        Moka.getContext().addScene(new Example2());
        Moka.getContext().setMainScene(Example2.class);

        // create and start the application at 60 fps.
        app.create().start(60);
    }
}
