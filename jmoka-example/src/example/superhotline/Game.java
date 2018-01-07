package example.superhotline;

import com.moka.core.Application;
import com.moka.core.Moka;
import com.moka.input.Input;
import com.moka.scene.Scene;
import com.moka.scene.entity.Entity;
import example.superhotline.hotline.MoveTowards;

public class Game extends Scene {
    @Override
    public void onCreate() {
        newCamera("Main Camera", true);

        R.objects.player.add(R.prefabs.player.newEntity("Player"));
        Entity enemy = R.prefabs.enemy.newEntity("Enemy01");
    }

    @Override
    public void onPostCreate() {
    }

    @Override
    public void onUpdate() {
        Moka.getInput().getCursorWorldPos(R.objects.mousePosition);
    }

    @Override
    public void onLeave() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onExit() {
    }

    public static void main(String[] args) {
        Application app = new Application(new R("jmoka-example/assets/"));

        // set some inputs.
        Moka.getInput().bindButton(R.buttons.FIRE_1, Input.KEY_SPACE);
        Moka.getInput().bindAxes(R.axes.HORIZONTAL, Input.KEY_A, Input.KEY_D);
        Moka.getInput().bindAxes(R.axes.VERTICAL, Input.KEY_S, Input.KEY_W);

        // set display options.
        Moka.getDisplay().createDisplay(R.screen.WIDTH, R.screen.HEIGHT, "New");

        // use and export the manifest.
        Moka.getNameManager().usePackage(
                "Hotline",
                "jmoka-example/src/",
                "example.superhotline.hotline",
                true
        );

        // set up scenes.
        Moka.getContext().addScene(new Game());
        Moka.getContext().setMainScene(Game.class);

        // create and start the application at 60 fps.
        app.create().start(60);
    }
}
