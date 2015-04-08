package com.moka.core.game;

import com.moka.core.Context;
import com.moka.exceptions.JMokaException;

public class XmlGame extends Context {
	private final String xml;

	public XmlGame(String xml, String resPath) {
		if(xml == null)
			throw new JMokaException("Xml file path cannot be null.");

		this.xml = xml;

		if(resPath != null)
			define(resPath);
	}

	@Override
	public final void onCreate() {
		populate(xml);
	}

	@Override
	public final void onStop() {

	}
}
