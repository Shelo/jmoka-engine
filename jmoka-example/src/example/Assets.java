package example;

import com.moka.graphics.Texture;
import com.moka.resources.Resources;

public class Assets extends Resources
{
    public static Texture player;

    public void load()
    {
        player = texture("img/playerPlane.png");
    }
}
