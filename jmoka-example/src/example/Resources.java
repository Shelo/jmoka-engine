package example;

import com.moka.graphics.Texture;
import com.moka.resources.MokaResources;

public class Resources extends MokaResources
{
    public static class textures
    {
        public static Texture player;
    }

    public static class buttons
    {
        public static final String FIRE_BUTTON = "fire";
    }

    public void load()
    {
        textures.player = texture("img/playerPlane.png");
    }
}
