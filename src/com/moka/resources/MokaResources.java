package com.moka.resources;

import com.moka.core.Prefab;
import com.moka.graphics.Texture;

public abstract class MokaResources
{
    // TODO: CHANGE THE JMOKA-EXAMPLE THING!!!.
    private static String ROOT = "jmoka-example/assets/";

    public Texture texture(String path)
    {
        return new Texture(ROOT + path);
    }

    public Prefab prefab(String path)
    {
        // TODO: implement.
        return null;
    }

    public abstract void load();
    public abstract void dispose();
}
