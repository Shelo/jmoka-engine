package example;

import com.moka.core.Prefab;
import com.moka.graphics.Texture;
import com.moka.core.MokaResources;

public class Resources extends MokaResources
{
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

    public static class integers
    {
        public static int someValue = 90;
    }

    public Resources(String root)
    {
        super(root);
    }

    @Override
    public void load()
    {
        textures.player = texture("img/kl103.png");
        textures.playerShooting = texture("img/kl103_shooting.png");

        prefabs.player = prefab("prefabs/player.oping");
    }

    @Override
    public void dispose()
    {

    }
}
