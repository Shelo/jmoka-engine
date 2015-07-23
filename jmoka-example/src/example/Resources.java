package example;

import com.moka.core.Prefab;
import com.moka.graphics.Texture;
import com.moka.core.MokaResources;

public class Resources extends MokaResources
{
    public Resources(String root)
    {
        super(root);
    }

    public static class axes
    {
        public static final String HORIZONTAL = "horizontal";
        public static final String VERTICAL = "vertical";
    }

    public static class buttons
    {
        public static final String FIRE_1 = "fire1";
        public static final String FIRE_2 = "fire2";
    }

    public static class textures
    {
        public static Texture player;
        public static Texture playerShooting;
    }

    public static class prefabs
    {
        public static Prefab player;
    }

    @Override
    public void load()
    {
        textures.player = texture("img/ship_kl103.png");
        textures.playerShooting = texture("img/ship_kl103-shooting.png");

        prefabs.player = prefab("prefabs/player.oping");
    }

    @Override
    public void dispose()
    {

    }
}
