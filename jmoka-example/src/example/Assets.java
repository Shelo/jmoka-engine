package example;

import com.moka.graphics.Texture;
import com.moka.resources.Resources;

public class Assets extends Resources
{
    public static class textures
    {
        public static Texture player;
    }

    public void load()
    {
        textures.player = texture("img/playerPlane.png");
    }
}
