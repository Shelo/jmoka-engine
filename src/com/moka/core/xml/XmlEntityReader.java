package com.moka.core.xml;

import com.moka.components.Component;
import com.moka.core.Entity;
import com.moka.core.Resources;
import com.moka.core.game.BaseGame;
import com.moka.exceptions.JMokaException;
import com.moka.math.Vector2;
import com.moka.math.Vector3;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class XmlEntityReader {
	private static final String TAG_ENTITY 		= "entity";
	private static final int STATE_INIT			= 0;
	private static final int STATE_ENTITY		= 1;
	private static final int STATE_CLOSED		= 2;
	private static final String VAL_SIZE 		= "size";
	private static final String VAL_POSITION 	= "position";
	private static final String VAL_ROTATION 	= "rotation";
	private static final String VAL_LAYER 		= "layer";
	private static final char CHAR_REFERENCE 	= '@';

	private ParametersParser parametersParser;
	private String entityName;
	private SAXParser parser;
	private BaseGame game;
	private Entity entity;
	private int state;

	private class Handler extends DefaultHandler {
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			// if the parser is currently closed, throw exception.
			if(state == STATE_CLOSED) JMokaException.raise("Parser is currently closed.");

			// the parser needs to be in the init state to be able to read the entity tag.
			if(state == STATE_INIT && qName.equals(TAG_ENTITY)) {
				state = STATE_ENTITY;
				entity = game.newEntity(entityName);
				setTransformParams(entity, attributes);
			} else {
				// if the state is not equals to entity state then we know there's is an error with the XML file.
				if(state != STATE_ENTITY) JMokaException.raise("XML File corrupted.");

				// if we are in entity state then we can read components.
				Component component = readComponent(qName, attributes);
				entity.addComponent(component);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if(qName.equals(TAG_ENTITY))
				state = STATE_CLOSED;
		}
	}

	public XmlEntityReader(BaseGame game) {
		this.game = game;

		// the parser needs to be closed in order to read a new file.
		state = STATE_CLOSED;

		parametersParser = new ParametersParser();

		// create sax parser.
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
		} catch(ParserConfigurationException | SAXException e) {
			JMokaException.raise("Error with SAXParser.");
		}
	}

	/**
	 * Read and load an entity XML file.
	 * @param filePath	path to the XML file.
	 * @param name		name for the new entity.
	 * @return the created entity.
	 */
	public Entity read(String filePath, String name) {
		if(state != STATE_CLOSED) JMokaException.raise("XmlEntityReader's state is not init.");

		// first state is init.
		state 		= STATE_INIT;
		entity 		= null;
		entityName 	= name;

		try {
			// parse XML document.
			InputStream stream = new FileInputStream(filePath);
			parser.parse(stream, new Handler());
		} catch(FileNotFoundException e) {
			JMokaException.raise("File not found: " + filePath);
		} catch(SAXException e) {
			JMokaException.raise("File " + filePath + " is corrupted.");
		} catch(IOException e) {
			JMokaException.raise("IOException while reading " + filePath);
		}

		return entity;
	}

	public Component readComponent(String name, Attributes attributes) {
		String componentName = hasPackage(name) ? name : "com.moka.components." + name;

		Component component = null;
		
		try {
			// create a new instance of the component.
			Class<?> componentClass = Class.forName(componentName);
			component = (Component) componentClass.newInstance();

			// get component methods in order to search for attribute qualified ones.
			// meaning that the have the XmlAttribute annotation.
			ArrayList<Method> methods = getQualifiedMethods(componentClass);

			// run through all the qualified method an set attributes if they're present.
			for(Method method : methods)
				handleAttribute(component, method, name, attributes);

		} catch(ClassNotFoundException e) {
			throw new JMokaException("Component class " + name + " not found.");
		} catch(InstantiationException e) {
			throw new JMokaException("Component class " + name + " probably doesn't have a non-args constructor.");
		} catch(IllegalAccessException e) {
			throw new JMokaException("Component class " + name + " is not accessible.");
		}
		
		return component;
	}

	private void handleAttribute(Component component, Method method, String componentName, Attributes attributes) {
		XmlAttribute attribute = method.getAnnotation(XmlAttribute.class);
		String value = attributes.getValue(attribute.value());

		// if the value is not present we can either ignore it or, if the XmlAttribute
		// specifies that is required, throw an exception.
		if(value == null) {
			if(attribute.required())
				throw new JMokaException("Component " + componentName + " requires the attribute " + attribute.value());
			return;
		}

		// here the value is always something.
		// we should always take the first parameter type because these methods are supposed to have only one.
		Class<?> param = getParamFor(method);

		// test the value and cast it to the parameter's class.
		Object casted = getTestedValue(param, value);

		try {
			method.invoke(component, casted);
		} catch(IllegalAccessException e) {
			throw new JMokaException(String.format("Method %s for component %s is inaccessible",method.getName(),
					componentName));
		} catch(InvocationTargetException e) {
			throw new JMokaException("Invocation target exception!.");
		}
	}

	// test the value to match the param class, also if the param type is a Entity, this will find that
	// entity for you. If the value is a reference to a resource value, that value will be searched and delivered.
	@SuppressWarnings("unchecked")
	private <T> T getTestedValue(Class<T> param, String value) {
		if(value.charAt(0) == CHAR_REFERENCE) {
			String resource = value.substring(1);
			Object result = null;

			if(param == int.class) 			result = ((Number) Resources.get(resource)).intValue();
			else if(param == float.class) 	result = ((Number) Resources.get(resource)).floatValue();
			else if(param == double.class) 	result = ((Number) Resources.get(resource)).doubleValue();
			else if(param == boolean.class)	result = Resources.getBoolean(resource);
			else if(param == String.class)	result = Resources.getString(resource);
			else if(param == Entity.class)	result = game.findEntity(Resources.getString(resource));

			return (T) result;
		} else {
			Object result = null;

			if(param == int.class) 			result = Integer.parseInt(value);
			else if(param == float.class) 	result = Float.parseFloat(value);
			else if(param == double.class) 	result = Double.parseDouble(value);
			else if(param == boolean.class)	result = Boolean.parseBoolean(value);
			else if(param == String.class)	result = value;
			else if(param == Entity.class)	result = game.findEntity(value);

			return (T) result;
		}
	}

	private Class<?> getParamFor(Method method) {
		Class<?>[] params = method.getParameterTypes();

		// so, if the quantity of parameters is not equal to one, there's an error in the definition of the method.
		if(params.length != 1)
			throw new JMokaException(String.format("Method %s for component %s has more or less than one parameter," +
					"this is not allowed.", method.getName(), method.getDeclaringClass().getName()));

		return params[0];
	}

	private ArrayList<Method> getQualifiedMethods(Class<?> componentClass) {
		ArrayList<Method> qualified = new ArrayList<>();
		Method[] methods = componentClass.getDeclaredMethods();
		for(Method method : methods) {
			XmlAttribute attribute = method.getAnnotation(XmlAttribute.class);

			// if the attribute annotation is not null, then the method is Xml Qualified,
			// so it can be added to the resulting list.
			if(attribute != null) qualified.add(method);
		}
		return qualified;
	}

	public void setTransformParams(Entity entity, Attributes attributes) {
		String name = entity.getName();

		if(attributes.getValue(VAL_POSITION) != null) {
			try {
				String[] params = parametersParser.parse(attributes.getValue(VAL_POSITION));

				float layer = attributes.getValue(VAL_LAYER) == null? 0 :
						getTestedValue(int.class, attributes.getValue(VAL_LAYER));
				
				float x = getTestedValue(float.class, params[0]);
				float y = getTestedValue(float.class, params[1]);

				System.out.println(layer);

				entity.getTransform().setPosition(new Vector3(x, y, layer));
			} catch(ParsingException e) {
				JMokaException.raise("Entity: " + name + "'s position is malformed, must be Vector3.");
			}
		}

		if(attributes.getValue(VAL_ROTATION) != null) {
			float rotation = getTestedValue(float.class, attributes.getValue(VAL_ROTATION));
			entity.getTransform().setRotation(rotation);
		}

		if(attributes.getValue(VAL_SIZE) != null) {
			try {
				String[] params = parametersParser.parse(attributes.getValue(VAL_SIZE));

				float x = getTestedValue(float.class, params[0]);
				float y = getTestedValue(float.class, params[1]);

				entity.getTransform().setSize(new Vector2(x, y));
			} catch(ParsingException e) {
				JMokaException.raise("Entity: " + name + "'s size is malformed, must be Vector2.");
			}
		}
	}

	private boolean hasPackage(String componentClass) {
		return componentClass.split("\\.").length != 1;
	}
}
