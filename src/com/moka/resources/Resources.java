package com.moka.resources;

import com.moka.graphics.Texture;

public abstract class Resources
{
    public static Texture texture(String path)
    {
        return new Texture(path);
    }

    public abstract void load();
}
