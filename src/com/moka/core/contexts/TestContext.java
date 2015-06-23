package com.moka.core.contexts;

import com.moka.core.readers.EntityReader;
import com.moka.core.readers.PrefabReader;
import com.moka.core.readers.SceneReader;

public class TestContext extends Context
{
    @Override
    public EntityReader getEntityReader()
    {
        return null;
    }

    @Override
    public PrefabReader getPrefabReader()
    {
        return null;
    }

    @Override
    public SceneReader getSceneReader()
    {
        return null;
    }

    @Override
    public void onCreate()
    {

    }

    @Override
    public void onStop()
    {

    }
}
