package example;

import com.moka.core.Prefab;
import com.moka.graphics.Texture;
import com.moka.core.MokaResources;

public class Resources extends MokaResources
{
    public static class buttons
    {
        public static final String FIRE_1 = "fire1";
        public static final String FIRE_2 = "fire2";
        public static final String LEFT = "left";
        public static final String RIGHT = "right";
        public static final String UP = "up";
        public static final String DOWN = "down";
    }

    public static class textures
    {
        public static Texture player;
    }

    public static class prefabs
    {
        public static Prefab player;
    }

    @Override
    public void load()
    {
        textures.player = texture("img/playerPlane.png");
        prefabs.player = prefab("prefabs/player.oping");
    }

    @Override
    public void dispose()
    {

    }
}
