package com.moka.core.contexts;

import com.moka.core.Context;
import com.moka.utils.JMokaException;

public class XmlContext extends Context {
	private final String resPath;
	private final String xml;

	public XmlContext(String xml, String resPath)
	{
		if(xml == null)
		{
			throw new JMokaException("Xml file path cannot be null.");
		}

		this.xml = xml;
		this.resPath = resPath;
	}

	@Override
	public void onPreLoad()
	{
		if(resPath != null)
		{
			define(resPath);
		}
	}

	@Override
	public final void onCreate()
	{
		populate(xml);
	}

	@Override
	public final void onStop()
	{

	}
}
