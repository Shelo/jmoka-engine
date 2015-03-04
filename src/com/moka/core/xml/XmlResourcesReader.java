package com.moka.core.xml;

import com.moka.core.Resources;
import com.moka.exceptions.JMokaException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlResourcesReader {
	private static final String TAG_BOOLEAN = "boolean";
	private static final String TAG_INTEGER = "integer";
	private static final String TAG_VALUES 	= "values";
	private static final String TAG_STRING 	= "string";
	private static final String TAG_DOUBLE 	= "double";
	private static final String TAG_FLOAT 	= "float";
	private static final String TAG_RES 	= "res";

	private static enum STATE { NONE, VALUES, STRINGS, INTEGERS, FLOATS, DOUBLES, BOOLEANS }

	private class Helper extends DefaultHandler {
		private STATE state = STATE.NONE;

		@Override
		public void startDocument() throws SAXException {

		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			switch(state) {
				case NONE:
					if(qName.equals(TAG_VALUES)) {
						state = STATE.VALUES;
					} else {
						throw new JMokaException("Malformed Values XML file.");
					}
					break;
				case VALUES:
					break;
				case STRINGS:
					break;
				case INTEGERS:
					break;
				case FLOATS:
					break;
				case DOUBLES:
					break;
				case BOOLEANS:
					break;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {

		}

		@Override
		public void endDocument() throws SAXException {

		}
	}
	
	public XmlResourcesReader() {
		
	}
}
