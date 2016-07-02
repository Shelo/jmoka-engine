package example.integration;

import com.moka.components.TileMap;
import com.moka.core.Application;
import com.moka.core.Moka;
import com.moka.input.Input;
import com.moka.scene.Scene;
import com.moka.scene.entity.Entity;

public class Game extends Scene
{
    private Entity tileMapEntity;

    @Override
    public void onCreate()
    {
        newCamera("Main Camera", true);


        EnemyFactory enemyFactory = new EnemyFactory(R.data.enemies);
        enemyFactory.spawnHorde();

        R.entities.tiles.add(R.prefabs.enemy.newEntity(null, false));

        tileMapEntity = R.prefabs.tileMap.newEntity("TileMap");

        R.prefabs.player.newEntity("Player");
    }

    @Override
    public void onPostCreate()
    {
        TileMap tileMap = tileMapEntity.getComponent(TileMap.class);
        tileMap.line(0, 0, 10, 1, (byte) 0);
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

    public static void main(String[] args)
    {
        Application app = new Application(new R("jmoka-example/assets/"));

        // set some inputs.
        Moka.getInput().bindButton(R.buttons.FIRE_1, Input.KEY_SPACE);
        Moka.getInput().bindAxes(R.axes.HORIZONTAL, Input.KEY_A, Input.KEY_D);
        Moka.getInput().bindAxes(R.axes.VERTICAL, Input.KEY_S, Input.KEY_W);

        // set display options.
        Moka.getDisplay().createDisplay(R.screen.WIDTH, R.screen.HEIGHT, "Integration");

        Moka.getNameManager().usePackage(
                "Integration",
                "jmoka-example/src/",
                "example.integration.integration",
                true
        );

        // set up scenes.
        Moka.getContext().addScene(new Game());
        Moka.getContext().setMainScene(Game.class);

        // create and start the application at 60 fps.
        app.create().start(60);
    }
}
