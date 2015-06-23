package com.moka.core.contexts;

import com.moka.core.Prefab;
import com.moka.core.entity.Entity;
import com.moka.core.readers.EntityReader;
import com.moka.core.readers.PrefabReader;
import com.moka.core.readers.SceneReader;
import com.moka.core.readers.xml.XmlEntityReader;
import com.moka.core.readers.xml.XmlPrefabReader;
import com.moka.core.readers.xml.XmlSceneReader;

public class XmlContext extends ReaderContext
{
    private EntityReader entityReader;
    private PrefabReader prefabReader;
    private SceneReader sceneReader;

    public XmlContext(String scene, String resources)
    {
        super(scene, resources);

        // initialize readers.
        sceneReader = new XmlSceneReader(this);
        entityReader = sceneReader.getEntityReader();
        prefabReader = new XmlPrefabReader((XmlEntityReader) entityReader, this);
    }

    @Override
    public EntityReader getEntityReader()
    {
        return entityReader;
    }

    @Override
    public PrefabReader getPrefabReader()
    {
        return prefabReader;
    }

    @Override
    public SceneReader getSceneReader()
    {
        return sceneReader;
    }
}
