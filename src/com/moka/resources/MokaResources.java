package com.moka.resources;

import com.moka.graphics.Texture;

public abstract class MokaResources
{
    // TODO: CHANGE THE JMOKA-EXAMPLE THING!!!.
    private static String ROOT = "jmoka-example/assets/";

    public static Texture texture(String path)
    {
        return new Texture(ROOT + path);
    }

    public abstract void load();
}
