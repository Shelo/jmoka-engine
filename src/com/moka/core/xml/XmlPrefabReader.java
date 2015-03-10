package com.moka.core.xml;

import com.moka.core.Context;
import com.moka.core.Prefab;
import com.moka.exceptions.JMokaException;
import com.moka.math.Vector2f;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class XmlPrefabReader {
	private XmlEntityReader entityReader;
	private Context context;

	private class Handler extends DefaultHandler {
		private static final String TAG_ENTITY = "entity";

		Prefab.PreComponents preComponents;
		private Prefab prefab;

		public Handler(Prefab prefab, Prefab.PreComponents preComponents) {
			this.preComponents = preComponents;
			this.prefab = prefab;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {

			if (qName.equals(TAG_ENTITY)) {
				if (attributes.getValue(XmlEntityReader.VAL_POSITION) != null) {
					Vector2f position = entityReader.readPositionValues(attributes);
					prefab.setPosition(position);
				}

				if (attributes.getValue(XmlEntityReader.VAL_LAYER) != null) {
					int layer = entityReader.readLayer(attributes);
					prefab.setLayer(layer);
				}

				if (attributes.getValue(XmlEntityReader.VAL_ROTATION) != null) {
					float rotation = entityReader.readRotation(attributes);
					prefab.setRotation(rotation);
				}

				if (attributes.getValue(XmlEntityReader.VAL_SIZE) != null) {
					Vector2f size = entityReader.readSizeValues(attributes);
					prefab.setSize(size);
				}
			} else {
				Class<?> componentClass = entityReader.forComponent(qName);
				Prefab.ComponentAttrs attrs = new Prefab.ComponentAttrs();

				ArrayList<Method> qMethods = entityReader.getQualifiedMethods(componentClass);

				for(Method method : qMethods) {
					XmlAttribute attribute = method.getAnnotation(XmlAttribute.class);
					String value = attributes.getValue(attribute.value());

					if (value == null) {
						if (attribute.required())
							throw new JMokaException("Component " + qName + " requires the " +
									"attribute " + attribute.value());
					} else {
						Class<?> param = entityReader.getParamFor(method);
						Object casted = entityReader.getTestedValue(param, value);
						attrs.addMethodValue(method, casted);
					}
				}

				preComponents.put(componentClass, attrs);
			}
			
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			
		}
	}

	public XmlPrefabReader(XmlEntityReader entityReader, Context context) {
		this.entityReader = entityReader;
		this.context = context;
	}

	/**
	 * Returns a prefab object given a XML file path. The XML definition must follow the rules
	 * specified in <i>http://www.mokadev.com/entity</i>. If the XML file is malformed this will
	 * fail with an JMokaException, causing the program to crash, this is intended so the client
	 * can easily clear bugs with the information given from the exceptions.
	 *
	 * @param filePath path to the XML file.
	 * @return the prefab object.
	 */
	public Prefab newPrefab(String filePath) {
		Prefab.PreComponents componentAttrs = new Prefab.PreComponents();
		Prefab prefab = new Prefab(context, componentAttrs);
		Handler handler = new Handler(prefab, componentAttrs);

		try {
			entityReader.getParser().parse(filePath, handler);
		} catch(SAXException | IOException e) {
			throw new JMokaException("SAX parser exception: " + e.toString());
		}

		return prefab;
	}
}
