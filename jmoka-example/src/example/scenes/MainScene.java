package example.scenes;

import com.moka.core.Scene;
import com.moka.core.entity.Entity;
import example.Resources;
import example.components.nonsense.ExampleSceneLoader;
import example.components.spaceshooter.Movement;

public class MainScene extends Scene
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);
        Entity entity = newEntity("Player", Resources.textures.player, 0);

        entity.getTransform().move(400, 225);
        entity.addComponent(new ExampleSceneLoader()).addComponent(new Movement());
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
}
