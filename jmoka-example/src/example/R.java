package example;

import com.moka.resources.BindLoad;
import com.moka.prefabs.Prefab;
import com.moka.graphics.Texture;
import com.moka.resources.Resources;
import com.moka.utils.ConfigDataFile;
import com.moka.utils.FileHandle;

import java.io.File;

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
        public static final String FIRE_2 = "fire2";
    }

    public static class screen
    {
        public static int width = 800;
        public static int height = 450;
    }

    @BindLoad(path = "img/", extension = "png")
    public static class textures
    {
        public static Texture player;
        public static Texture playerShooting;

        public static Texture enemy01;
        public static Texture enemy02;
        public static Texture enemy03;

        public static Texture bullet01;
        public static Texture space01;

        public static Texture explosion01;
        public static Texture explosion02;

        public static Texture blank;

        @BindLoad(path = "tiles/", extension = "png")
        public static class tiles
        {
            public static Texture dirt01;
        }
    }

    @BindLoad(path = "save/", extension = "txt")
    public static class save
    {
        public static ConfigDataFile user;
    }

    @BindLoad(skip = true, path = "prefabs/", extension = "oping")
    public static class prefabs
    {
        public static Prefab player;

        public static Prefab enemy01;
        public static Prefab enemy02;
        public static Prefab enemy03;

        public static Prefab bullet01;

        public static Prefab space01;

        public static Prefab explosion01;
        public static Prefab explosion02;

        public static Prefab blocky;
        public static Prefab ball;

        @BindLoad(path = "tiles/", extension = "oping")
        public static class tiles
        {
            public static Prefab dirt01;
        }
    }

    public R(String root)
    {
        super(root);
    }

    @Override
    public void load()
    {
        bindLoad(prefabs.class);
    }
}
