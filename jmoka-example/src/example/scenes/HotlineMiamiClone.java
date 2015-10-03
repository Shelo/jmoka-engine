package example.scenes;

import com.moka.components.LookAt;
import com.moka.core.Moka;
import com.moka.scene.Scene;
import com.moka.scene.entity.Entity;
import example.R;

public class HotlineMiamiClone extends Scene
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);
        Entity player = R.prefabs.hotliner.newEntity("Player");
    }

    @Override
    public void onUpdate()
    {
        R.objects.mousePosition.set(Moka.getInput().getCursorPos());
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
