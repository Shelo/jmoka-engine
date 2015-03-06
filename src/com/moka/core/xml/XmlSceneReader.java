package com.moka.core.xml;

import com.moka.components.Component;
import com.moka.core.BaseGame;
import com.moka.core.Entity;
import com.moka.exceptions.JMokaException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XmlSceneReader {
	private static final String TAG_INCLUDE = "include";
	private static final String TAG_ENTITY 	= "entity";
	private static final String TAG_ROOT 	= "scene";
	private static final String KEY_NAME 	= "name";
	private static final String KEY_PATH 	= "path";

	private XmlEntityReader entityReader;
	private String currentFilePath;
	private Entity currentEntity;
	private SAXParser parser;
	private BaseGame game;

	private class Handler extends DefaultHandler {
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if(currentEntity == null) {
				// if the currentEntity is null then we should read "Entity" next,
				// if that is not the case, then the XML is malformed.
				if(qName.equals(TAG_ENTITY)) {
					// get and set the name for that entity.
					String name = attributes.getValue(KEY_NAME);
					currentEntity = game.newEntity(name);

					// set transform position, rotation and scale.
					entityReader.setTransformValues(currentEntity, attributes);

				} else if(qName.equals(TAG_INCLUDE)) {
					// with a include tag we should create an entity, put the name that it should have and
					// override possible transform properties.
					String path = attributes.getValue(KEY_PATH);
					String name = attributes.getValue(KEY_NAME);

					Entity entity = entityReader.read(path, name);
					entityReader.setTransformValues(entity, attributes);
				} else if(!qName.equals(TAG_ROOT)) {
					throw new JMokaException("XML: " + currentFilePath + " is malformed.");
				}
			} else {
				Component component = entityReader.readComponent(qName, attributes);
				currentEntity.addComponent(component);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if(qName.equals(TAG_ENTITY))
				currentEntity = null;
		}

		@Override
		public void endDocument() throws SAXException {
			currentEntity = null;
			currentFilePath = null;
			game = null;
		}
	}

	public XmlSceneReader(BaseGame game) {
		this.game = game;

		entityReader = new XmlEntityReader(game);

		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
		} catch(ParserConfigurationException | SAXException e) {
			throw new JMokaException("SAXParser creation error.");
		}
	}

	public void read(String filePath) {
		try {

			InputStream stream = new FileInputStream(filePath);
			currentFilePath = filePath;
			parser.parse(stream, new Handler());

		} catch(SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public XmlEntityReader getEntityReader() {
		return entityReader;
	}
}
