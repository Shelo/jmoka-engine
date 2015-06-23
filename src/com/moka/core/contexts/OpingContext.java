package com.moka.core.contexts;

import com.moka.core.readers.EntityReader;
import com.moka.core.readers.PrefabReader;
import com.moka.core.readers.SceneReader;
import com.moka.core.readers.oping.OpingEntityReader;

public class OpingContext extends ReaderContext
{
    private SceneReader sceneReader;
    private EntityReader entityReader;
    private PrefabReader prefabReader;

    public OpingContext(String scene, String resources)
    {
        super(scene, resources);

        entityReader = new OpingEntityReader(this);
    }

    @Override
    public SceneReader getSceneReader()
    {
        return sceneReader;
    }

    @Override
    public PrefabReader getPrefabReader()
    {
        return prefabReader;
    }

    @Override
    public EntityReader getEntityReader()
    {
        return entityReader;
    }
}
