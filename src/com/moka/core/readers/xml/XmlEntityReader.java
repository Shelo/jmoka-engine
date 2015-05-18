package com.moka.core.readers.xml;

import com.moka.core.*;
import com.moka.core.contexts.Context;
import com.moka.core.entity.Component;
import com.moka.core.entity.Entity;
import com.moka.core.readers.ComponentAttribute;
import com.moka.core.readers.EntityReader;
import com.moka.core.readers.PendingTransaction;
import com.moka.math.Vector2;
import com.moka.triggers.Trigger;
import com.moka.utils.JMokaException;
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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class XmlEntityReader extends EntityReader
{
    public static final int STATE_INIT = 0;
    public static final int STATE_ENTITY = 1;
    public static final int STATE_CLOSED = 2;
    public static final String VAL_SIZE = "size";
    public static final String VAL_POSITION = "position";
    public static final String VAL_ROTATION = "rotation";
    public static final String VAL_LAYER = "layer";
    public static final char CHAR_REFERENCE = '@';
    public static final char CHAR_EXPRESSION = '$';

    private String entityName;
    private SAXParser parser;
    private Entity entity;
    private int state;

    /**
     * Reads an entity XML File.
     */
    private class Handler extends DefaultHandler
    {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException
        {
            // if the parser is currently closed, throw exception.
            if (state == STATE_CLOSED)
            {
                throw new JMokaException("Parser is currently closed.");
            }

            // the parser needs to be in the init state to be able to read the entity tag.
            if (state == STATE_INIT && qName.equals(XmlSceneReader.TAG_ENTITY))
            {
                state = STATE_ENTITY;
                entity = context.newEntity(entityName, readLayer(attributes));
                setTransformValues(entity.getTransform(), attributes);

                String group = attributes.getValue(XmlSceneReader.KEY_GROUP);

                entity.setGroup(group);
            }
            else
            {
                // if the state is not equals to entity state then we know there's is an error with
                // the XML file.
                if (state != STATE_ENTITY)
                {
                    throw new JMokaException("XML File corrupted.");
                }

                // if we are in entity state then we can read components.
                readComponent(entity, qName, attributes);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException
        {
            if (qName.equals(XmlSceneReader.TAG_ENTITY))
            {
                state = STATE_CLOSED;
            }
        }
    }

    /**
     * Creates a new reader for a given context.
     *
     * @param context the context that will receive new entities.
     */
    public XmlEntityReader(Context context)
    {
        super(context);

        // the parser needs to be closed in order to read a new file.
        state = STATE_CLOSED;

        // create SAX parser.
        try
        {
            parser = SAXParserFactory.newInstance().newSAXParser();
        }
        catch (ParserConfigurationException | SAXException e)
        {
            throw new JMokaException("Error SAXParser: " + e.toString());
        }
    }

    @Override
    public Entity read(String filePath, String name)
    {
        entityName = name;
        entity = null;
        state = STATE_INIT;

        try
        {
            // parse XML document.
            InputStream stream = new FileInputStream(filePath);
            parser.parse(stream, new Handler());
        }
        catch (FileNotFoundException e)
        {
            throw new JMokaException("File not found: " + filePath);
        }
        catch (SAXException e)
        {
            throw new JMokaException("File " + filePath + " is corrupted.");
        }
        catch (IOException e)
        {
            throw new JMokaException("IOException while reading " + filePath);
        }

        return entity;
    }

    /**
     * Reads a component and adds it to a given entity.
     *
     * @param entity     the entity that will have the component.
     * @param name       the name of the component.
     * @param attributes XML attributes for the component.
     */
    public void readComponent(Entity entity, String name, Attributes attributes)
    {
        Component component = null;

        try
        {
            // create a new instance of the component.
            Class<?> componentClass = forComponent(name);
            component = (Component) componentClass.newInstance();
            entity.addComponent(component);

            // get component methods in order to search for attribute qualified ones.
            // meaning that the have the ComponentAttribute annotation.
            ArrayList<Method> methods = getQualifiedMethods(componentClass);

            // run through all the qualified method an set attributes if they're present.
            for (Method method : methods)
            {
                handleAttribute(component, method, attributes);
            }
        }
        catch (InstantiationException e)
        {
            throw new JMokaException("Component class " + name + " probably doesn't have a" +
                    "non-args constructor.");
        }
        catch (IllegalAccessException e)
        {
            throw new JMokaException("Component class " + name + " is not accessible.");
        }
    }

    /**
     * Handles an attribute of a component. As for example: the Sprite component receives
     * a texture string. This method will get that attribute and call the method on the
     * given component in order to set the texture properly.
     *
     * This method can handle entities references, triggers and prefabs.
     *
     * @param component  the target component.
     * @param method     the method that will be called.
     * @param attributes the attribute object of the component, specified on the XML file.
     */
    @SuppressWarnings("unchecked")
    private void handleAttribute(Component component, Method method, Attributes attributes)
    {
        ComponentAttribute attribute = method.getAnnotation(ComponentAttribute.class);
        String value = attributes.getValue(attribute.value());

        // if the value is not present we can either ignore it or, if the ComponentAttribute
        // specifies that is required, throw an exception.
        if (!validateAttribute(attribute, value, component.getClass().getSimpleName()))
            return;

        invokeMethodOnComponent(component, method, value);
    }

    public boolean validateAttribute(ComponentAttribute xmlAttr, String value, String componentName)
    {
        if (value == null)
        {
            if (xmlAttr.required())
            {
                throw new JMokaException("Component " + componentName + " requires the '" + xmlAttr.value()
                        + "' attribute.");
            }

            return false;
        }

        return true;
    }

    /**
     * Returns the parser used by this reader. This prevent the creation of additional parsers.
     *
     * @return this reader's parser.
     */
    public SAXParser getParser()
    {
        return parser;
    }

    /**
     * Set the transform values of the given {@link Entity}, casting them from
     * Strings, evaluating expressions and resolving references, in any case, this is mostly
     * used by this class internally when reading the XML.
     *
     * @param transform  the transform.
     * @param attributes the attributes that will be applied, all of them Strings.
     */
    public void setTransformValues(Transform transform, Attributes attributes)
    {
        if (attributes.getValue(VAL_POSITION) != null)
        {
            Vector2 position = readPositionValues(attributes);
            transform.setPosition(new Vector2(position.x, position.y));
        }

        if (attributes.getValue(VAL_ROTATION) != null)
        {
            float rotation = readRotation(attributes);
            transform.setRotation((float) Math.toRadians(rotation));
        }

        if (attributes.getValue(VAL_SIZE) != null)
        {
            Vector2 size = readSizeValues(attributes);
            transform.setSize(size);
        }
    }

    /**
     * Reads the position given an attributes object.
     *
     * @param attributes the attributes objects of an entity.
     * @return the position as a 2D vector.
     */
    public Vector2 readPositionValues(Attributes attributes)
    {
        String[] params = attributes.getValue(VAL_POSITION).split(" *, *");
        float x = getTestedValue(float.class, params[0]);
        float y = getTestedValue(float.class, params[1]);
        return new Vector2(x, y);
    }

    /**
     * Reads the layer attribute of an entity.
     *
     * @param attributes the attributes object of the entity.
     * @return the layer value.
     */
    public int readLayer(Attributes attributes)
    {
        String value = attributes.getValue(VAL_LAYER);
        return value == null ? 0 : getTestedValue(int.class, value);
    }

    /**
     * Reads the rotation attribute of an entity.
     *
     * @param attributes the attribute object.
     * @return the rotation float value.
     */
    public float readRotation(Attributes attributes)
    {
        return getTestedValue(float.class, attributes.getValue(VAL_ROTATION));
    }

    /**
     * Reads the size of an entity.
     *
     * @param attributes the attributes object of that entity.
     * @return the size as a 2D vector.
     */
    public Vector2 readSizeValues(Attributes attributes)
    {
        String[] params = attributes.getValue(VAL_SIZE).split(" *, *");
        float x = getTestedValue(float.class, params[0]);
        float y = getTestedValue(float.class, params[1]);
        return new Vector2(x, y);
    }

    /**
     * Gets the qualified methods of a given class, meaning, all methods that have an
     * {@link ComponentAttribute} annotation in it.
     *
     * @param componentClass the component class.
     * @return the list of qualified methods only.
     */
    public ArrayList<Method> getQualifiedMethods(Class<?> componentClass)
    {
        ArrayList<Method> qualified = new ArrayList<>();

        // obtain all methods declared by the component and any super class.
        Method[] methods = componentClass.getMethods();
        for (Method method : methods)
        {
            ComponentAttribute attribute = method.getAnnotation(ComponentAttribute.class);

            // if the attribute annotation is not null, then the method is Xml Qualified,
            // so it can be added to the resulting list.
            if (attribute != null)
            {
                qualified.add(method);
            }
        }

        return qualified;
    }

    @Override
    protected char getExpressionChar()
    {
        return CHAR_EXPRESSION;
    }

    @Override
    protected char getReferenceChar()
    {
        return CHAR_REFERENCE;
    }
}
