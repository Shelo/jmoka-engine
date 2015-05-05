package com.moka.core.contexts;

import com.moka.core.readers.SceneReader;

public class XmlContext extends ReaderContext
{
    public XmlContext(String xml, String resources)
    {
        super(xml, resources);
    }

    @Override
    protected SceneReader getSceneReader()
    {
        return sceneReader;
    }
}
