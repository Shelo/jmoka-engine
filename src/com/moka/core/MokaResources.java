package com.moka.core;

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
        return Moka.getContext().getPrefabReader().newPrefab(ROOT + path);
    }

    public void sound(String path)
    {
        // TODO: do something.
    }

    public abstract void load();
    public abstract void dispose();
}
