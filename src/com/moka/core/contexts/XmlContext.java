package com.moka.core.contexts;

public class XmlContext extends ReaderContext
{
    public XmlContext(String xml, String resources)
    {
        super(xml, resources);
    }

    @Override
    public final void onCreate()
    {
        populate(scene);
    }

    @Override
    public final void onStop()
    {

    }
}
