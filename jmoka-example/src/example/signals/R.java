package example.signals;

import com.moka.graphics.Texture;
import com.moka.prefabs.Prefab;
import com.moka.resources.BindLoad;
import com.moka.resources.Resources;

public class R extends Resources
{
    public static class buttons
    {
        public static final String FIRE_1 = "fire1";
    }

    public static class screen
    {
        public static int WIDTH = 800;
        public static int HEIGHT = 600;
    }

    @BindLoad(path = "img/", extension = "png")
    public static class textures
    {
        public static Texture demo;
    }

    @BindLoad(delay = true, path = "prefabs/", extension = "oping")
    public static class prefabs
    {
        public static Prefab demo;
    }

    public R(String root)
    {
        super(root);
    }
}
