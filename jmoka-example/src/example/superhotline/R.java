package example.superhotline;

import com.moka.graphics.Texture;
import com.moka.math.Vector2;
import com.moka.prefabs.Prefab;
import com.moka.resources.BindLoad;
import com.moka.resources.Resources;
import com.moka.resources.utils.EntityBuffer;
import com.moka.scene.entity.Entity;
import com.moka.utils.ConfigDataFile;

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
        public static int WIDTH = 64 * 16;
        public static int HEIGHT = 64 * 9;
    }

    @BindLoad(path = "img/", extension = "png")
    public static class textures
    {
        public static Texture player;
        public static Texture bullet01;
        public static Texture enemy02;
    }

    @BindLoad(delay = true, path = "prefabs/superhotline/", extension = "oping")
    public static class prefabs
    {
        public static Prefab bullet;
        public static Prefab player;
        public static Prefab enemy;
    }

    public static class objects {
        public static Vector2 mousePosition = new Vector2();
        public static EntityBuffer player = new EntityBuffer(1);
    }

    public R(String root)
    {
        super(root);
    }
}
