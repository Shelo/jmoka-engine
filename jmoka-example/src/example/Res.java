package example;

import com.moka.core.Prefab;
import com.moka.graphics.Texture;
import com.moka.core.MokaResources;

public class Res extends MokaResources
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

        public static Texture enemy01;
        public static Texture enemy02;
        public static Texture enemy03;

        public static Texture tileDirt01;
        public static Texture tileDirt02;
    }

    public static class prefabs
    {
        public static Prefab player;

        public static Prefab enemy01;
        public static Prefab enemy02;
        public static Prefab enemy03;

        public static Prefab tileDirt01;
        public static Prefab tileDirt02;
    }

    public static class integers
    {
        public static int screenWidth = 800;
        public static int screenHeight = 450;
    }

    public Res(String root)
    {
        super(root);
    }

    @Override
    public void load()
    {
        // Textures - player
        textures.player = texture("img/kl103.png");
        textures.playerShooting = texture("img/kl103_shooting.png");

        // Textures - enemies
        textures.enemy01 = texture("img/al01.png");
        textures.enemy02 = texture("img/al02.png");
        textures.enemy03 = texture("img/al03.png");

        // Textures - tiles
        textures.tileDirt01 = texture("img/tiles/dirt01.png");
        textures.tileDirt02 = texture("img/tiles/dirt02.png");


        // Prefabs - players
        prefabs.player = prefab("prefabs/player.oping");

        // Prefabs - enemies
        prefabs.enemy01 = prefab("prefabs/enemy01.oping");
        prefabs.enemy02 = prefab("prefabs/enemy02.oping");
        prefabs.enemy03 = prefab("prefabs/enemy03.oping");

        // Prefabs - tiles
        prefabs.tileDirt01 = prefab("prefabs/tile_dirt01.oping");
    }

    @Override
    public void dispose()
    {

    }
}
