package example.scenes;

import com.moka.components.TileMap;
import com.moka.core.Moka;
import com.moka.math.MathUtil;
import com.moka.math.Vector2;
import com.moka.scene.Scene;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.Entity;
import com.moka.utils.Pools;
import com.shelodev.oping2.structure.Leaf;
import example.R;

public class HotlineMiamiClone extends Scene
{
    @Override
    public void onCreate()
    {
        newCamera("MainCamera", true);
        Entity player = R.prefabs.hotliner.newEntity("Player", 2);

        if (R.save.player.exists()) {
            Leaf position = R.save.player.getRoot().getLeaf("Position");
            player.getTransform().setPosition(position.getFloat(0), position.getFloat(1));
        } else {
            R.save.player.getRoot().setName("Player");
            R.save.player.getRoot().addLeaf(new Leaf("Position", "0", "0"));
            R.save.player.save();
        }

        /*
        R.objects.tiles.add(R.prefabs.tiles.grass.newEntity(null));

        Entity entity = R.prefabs.tilemap.newEntity("TileMap", 1);
        TileMap tileMap = (TileMap) entity.getDrawable();
        tileMap.init();

        tileMap.line(0, 0, 200, 7, (byte) 0);
        */
    }

    @Override
    public void onUpdate()
    {
        Vector2 buffer = Pools.vec2.take();
        {
            R.objects.mousePosition.set(
                    Moka.getRenderer().getCamera().toWorldCoords(Moka.getInput().getCursorPos(), buffer));
        }
        Pools.vec2.put(buffer);
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
