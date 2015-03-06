package com.moka.core.xml;

import com.moka.components.Component;
import com.moka.core.Entity;
import com.moka.core.Prefab;
import com.moka.core.Resources;
import com.moka.core.BaseGame;
import com.moka.exceptions.JMokaException;
import com.moka.math.Vector2;
import com.moka.math.Vector3;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
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
import java.util.HashSet;

// TODO: clean this code a little bit.
public class XmlEntityReader {
	private static final String TAG_ENTITY 				= "entity";
	private static final int STATE_INIT					= 0;
	private static final int STATE_ENTITY				= 1;
	private static final int STATE_CLOSED				= 2;
	private static final String VAL_SIZE 				= "size";
	private static final String VAL_POSITION 			= "position";
	private static final String VAL_ROTATION 			= "rotation";
	private static final String VAL_LAYER 				= "layer";
	private static final char CHAR_REFERENCE 			= '@';
	private static final char CHAR_EXPRESSION 			= '$';
	private static final ArrayList<Character> SYMBOLS 	= new ArrayList<>();

	private Evaluator evaluator;
	private String entityName;
	private BaseGame baseGame;
	private SAXParser parser;
	private Entity entity;
	private int state;

	static {
		// fill the symbols table.
		SYMBOLS.add('/');
		SYMBOLS.add('+');
		SYMBOLS.add('-');
		SYMBOLS.add('*');
		SYMBOLS.add(' ');
		SYMBOLS.add('(');
		SYMBOLS.add(')');
		SYMBOLS.add('%');
	}

	private class Handler extends DefaultHandler {
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			// if the parser is currently closed, throw exception.
			if (state == STATE_CLOSED)
				throw new JMokaException("Parser is currently closed.");

			// the parser needs to be in the init state to be able to read the entity tag.
			if (state == STATE_INIT && qName.equals(TAG_ENTITY)) {
				state = STATE_ENTITY;
				entity = baseGame.newEntity(entityName);
				setTransformValues(entity, attributes);
			} else {
				// if the state is not equals to entity state then we know there's is an error with the XML file.
				if (state != STATE_ENTITY)
					throw new JMokaException("XML File corrupted.");

				// if we are in entity state then we can read components.
				entity.addComponent(readComponent(qName, attributes));
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equals(TAG_ENTITY))
				state = STATE_CLOSED;
		}
	}

	public XmlEntityReader(BaseGame baseGame) {
		this.baseGame = baseGame;

		// the parser needs to be closed in order to read a new file.
		state = STATE_CLOSED;

		// create the JEval evaluator.
		evaluator = new Evaluator();

		// create SAX parser.
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
		} catch(ParserConfigurationException | SAXException e) {
			throw new JMokaException("Error SAXParser: " + e.toString());
		}
	}

	/**
	 * Read and load an entity XML file.
	 * @param filePath	path to the XML file.
	 * @param name		name for the new entity.
	 * @return the created entity.
	 */
	public Entity read(String filePath, String name) {
		if (state != STATE_CLOSED)
			throw new JMokaException("XmlEntityReader's state is not init.");

		// first state is init.
		state = STATE_INIT;
		entity = null;
		entityName = name;

		try {
			// parse XML document.
			InputStream stream = new FileInputStream(filePath);
			parser.parse(stream, new Handler());
		} catch(FileNotFoundException e) {
			throw new JMokaException("File not found: " + filePath);
		} catch(SAXException e) {
			throw new JMokaException("File " + filePath + " is corrupted.");
		} catch(IOException e) {
			throw new JMokaException("IOException while reading " + filePath);
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
			for (Method method : methods)
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
		if (value == null) {
			if (attribute.required())
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
			throw new JMokaException(String.format("Method %s for component %s is inaccessible", method.getName(),
					componentName));
		} catch(InvocationTargetException e) {
			throw new JMokaException(componentName + " Invocation target exception!.");
		}
	}

	/**
	 * Test the value to match the param class, also if the param type is a Entity, this will find
	 * that entity for you. If the value is a reference to a resource value, that value will be
	 * searched and delivered, and finally, if the value is an expression, this will attempt to
	 * resolve it.
	 * 
	 * TODO: catch components with EntityName.ComponentClass.
	 *
	 * @param param	the parameter class
	 * @param value	the value that will be tested.
	 * @param <T>	the generic type of the parameter.
	 * @return 		the resulting value, one of: int, float, double, boolean, String or Entity.
	 */
	@SuppressWarnings("unchecked")
	private <T> T getTestedValue(Class<T> param, String value) {
		if (value.charAt(0) == CHAR_REFERENCE) {
			String resource = value.substring(1);
			Object result = null;

			if (param == int.class)
				result = ((Number) Resources.get(resource)).intValue();
			else if (param == float.class)
				result = ((Number) Resources.get(resource)).floatValue();
			else if (param == double.class)
				result = ((Number) Resources.get(resource)).doubleValue();
			else if (param == boolean.class)
				result = Resources.getBoolean(resource);
			else if (param == String.class)
				result = Resources.getString(resource);
			else if (param == Entity.class)
				result = baseGame.findEntity(Resources.getString(resource));

			return (T) result;
		} else if(value.charAt(0) == CHAR_EXPRESSION) {
			Object result = null;
			String expression = replaceReferences(value.substring(2, value.length() - 1));
			String expValue = null;

			try {
				expValue = evaluator.evaluate(expression);
			} catch(EvaluationException e) {
				throw new JMokaException("[JEval] " + e.toString());
			}

			if (param == int.class)
				result = Integer.parseInt(expValue);
			else if (param == float.class)
				result = Float.parseFloat(expValue);
			else if (param == double.class)
				result = Double.parseDouble(expValue);
			else if (param == boolean.class)
				result = Boolean.parseBoolean(expValue);

			return (T) result;
		} else {
			Object result = null;

			if (param == int.class)
				result = Integer.parseInt(value);
			else if (param == float.class)
				result = Float.parseFloat(value);
			else if (param == double.class)
				result = Double.parseDouble(value);
			else if (param == boolean.class)
				result = Boolean.parseBoolean(value);
			else if (param == String.class)
				result = value;
			else if (param == Entity.class)
				result = baseGame.findEntity(value);

			return (T) result;
		}
	}

	/**
	 * Set the transform values of the given {@link com.moka.core.Entity}, casting them from
	 * Strings, evaluating expressions and resolving references, in any case, this is mostly
	 * used by this class internally when reading the XML.
	 * @param entity		the entity with the transform.
	 * @param attributes	the attributes that will be applied, all of them Strings.
	 */
	public void setTransformValues(Entity entity, Attributes attributes) {
		if (attributes.getValue(VAL_POSITION) != null) {
			String[] params = attributes.getValue(VAL_POSITION).split(" *, *");
			float x = getTestedValue(float.class, params[0]);
			float y = getTestedValue(float.class, params[1]);
			float layer = attributes.getValue(VAL_LAYER) == null? 0 :
					getTestedValue(int.class, attributes.getValue(VAL_LAYER));

			entity.getTransform().setPosition(new Vector3(x, y, layer));
		}

		if (attributes.getValue(VAL_ROTATION) != null) {
			float rotation = getTestedValue(float.class, attributes.getValue(VAL_ROTATION));

			entity.getTransform().setRotation(rotation);
		}

		if (attributes.getValue(VAL_SIZE) != null) {
			String[] params = attributes.getValue(VAL_SIZE).split(" *, *");
			float x = getTestedValue(float.class, params[0]);
			float y = getTestedValue(float.class, params[1]);

			entity.getTransform().setSize(new Vector2(x, y));
		}
	}

	/**
	 * Obtains the parameter class for a given method, since the engine only allows to receive
	 * one parameter in an qualified method, if the method has more than one parameter, the program
	 * will crash so the client can fix this.
	 *
	 * @param method	the method.
	 * @return			the parameter's class.
	 */
	private Class<?> getParamFor(Method method) {
		Class<?>[] params = method.getParameterTypes();

		// so, if the quantity of parameters is not equal to one, there's an error in the definition
		// of the method.
		if (params.length != 1)
			throw new JMokaException(String.format("Method %s for component %s has more or less" +
					"than one parameter, this is not allowed.", method.getName(),
					method.getDeclaringClass().getName()));

		return params[0];
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
		// TODO.
		return null;
	}

	/**
	 * Gets the qualified methods of a given class, meaning, all methods that have an
	 * {@link XmlAttribute} annotation in it.
	 * @param componentClass	the component class.
	 * @return					the list of qualified methods only.
	 */
	private ArrayList<Method> getQualifiedMethods(Class<?> componentClass) {
		ArrayList<Method> qualified = new ArrayList<>();

		// obtain all methods declared by the component and any super class.
		Method[] methods = componentClass.getMethods();
		for (Method method : methods) {
			XmlAttribute attribute = method.getAnnotation(XmlAttribute.class);

			// if the attribute annotation is not null, then the method is Xml Qualified,
			// so it can be added to the resulting list.
			if (attribute != null)
				qualified.add(method);
		}

		return qualified;
	}

	private String replaceReferences(String expression) {
		StringBuilder curReference = new StringBuilder();
		HashSet<String> references = new HashSet<>();
		boolean rRef = false;

		for (int i = 0; i < expression.length(); i++) {
			char c = expression.charAt(i);

			if (rRef) {
				if (curReference.length() == 0) {
					if (Character.isLetter(c)) {
						curReference.append(c);
					} else {
						throw new JMokaException("Malformed math expression.");
					}
				} else {
					if (SYMBOLS.contains(c)) {
						rRef = false;
						references.add(curReference.toString());
						curReference.setLength(0);
					} else {
						curReference.append(c);
					}
				}
			} else {
				if (c == CHAR_REFERENCE)
					rRef = true;
			}
		}

		StringBuilder result = new StringBuilder(expression);
		for (String reference : references)
			replaceAll(result, CHAR_REFERENCE + reference, Resources.get(reference).toString());

		return result.toString();
	}

	private StringBuilder replaceAll(StringBuilder builder, String from, String to) {
		int index = builder.indexOf(from);
		while (index != -1) {
			builder.replace(index, index + from.length(), to);
			index += to.length();
			index = builder.indexOf(from, index);
		}

		return builder;
	}

	/**
	 * Checks if for a given component class is there any specified package route declared.
	 */
	private boolean hasPackage(String componentClass) {
		return componentClass.split("\\.").length != 1;
	}
}
