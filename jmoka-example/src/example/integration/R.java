package example.integration;

import com.moka.graphics.Texture;
import com.moka.prefabs.Prefab;
import com.moka.resources.BindLoad;
import com.moka.resources.Resources;

public class R extends Resources
{
    public static class axes
    {
        public static final String HORIZONTAL = "horizontal";
        public static final String VERTICAL = "vertical";
    }

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
        public static Texture player;
    }

    @BindLoad(path = "save/", extension = "oping")
    public static class save
    {

    }

    @BindLoad(delay = true, path = "prefabs/integration/", extension = "oping")
    public static class prefabs
    {
        public static Prefab player;
    }

    public R(String root)
    {
        super(root);
    }
}
