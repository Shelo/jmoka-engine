package example.scenes;

import com.moka.core.Moka;
import com.moka.scene.Scene;
import com.moka.scene.entity.Entity;
import com.shelodev.oping2.structure.Leaf;
import example.R;

public class HotlineMiamiClone extends Scene
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);
        Entity player = R.prefabs.hotliner.newEntity("Player");

        if (R.save.player.exists())
        {
            Leaf position = R.save.player.getRoot().getLeaf("Position");
            player.getTransform().setPosition(position.getFloat(0), position.getFloat(1));
        }
        else
        {
            R.save.player.getRoot().setName("Player");
            R.save.player.getRoot().addLeaf(new Leaf("Position", "0", "0"));
            R.save.player.save();
        }
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
        Entity entity = findEntity("Player");
        Leaf position = R.save.player.getRoot().getLeaf("Position");
        position.setValue(0, entity.getTransform().getPosition().x);
        position.setValue(1, entity.getTransform().getPosition().y);
        R.save.player.save();
    }
}
