package com.moka.core.game;

import com.moka.exceptions.JMokaException;

public class XmlGame extends BaseGame {
	private String xml;
	
	public XmlGame(String xml) {
		if(xml == null)
			throw new JMokaException("Xml file path cannot be null.");
		this.xml = xml;
	}
	
	@Override
	public final void onCreate() {
		String[] resources = getResources();
		if(resources != null)
			for(String resource : resources)
				define(resource);

		populate(xml);
	}

	@Override
	public final void onStop() {

	}

	public String[] getResources() {
		return null;
	}
}
