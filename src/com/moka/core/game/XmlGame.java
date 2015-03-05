package com.moka.core.game;

import com.moka.exceptions.JMokaException;

import java.io.IOException;

public class XmlGame extends BaseGame {
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
