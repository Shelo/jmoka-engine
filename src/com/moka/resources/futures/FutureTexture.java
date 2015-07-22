package com.moka.resources.futures;

import com.moka.graphics.Texture;
import com.moka.resources.FutureAsset;

public class FutureTexture extends FutureAsset
{
    private String path;

    public FutureTexture(String path)
    {
        this.path = path;
    }

    @Override
    public Object load()
    {
        return new Texture(path);
    }
}
