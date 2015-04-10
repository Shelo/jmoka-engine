package com.moka.core.xml;

import com.moka.components.Component;
import com.moka.core.Context;
import com.moka.core.Entity;
import com.moka.core.Prefab;
import com.moka.core.Transform;
import com.moka.core.triggers.Trigger;
import com.moka.utils.JMokaException;
import com.moka.math.Vector2f;
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
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashSet;

public class XmlEntityReader
{
    public static final String TAG_ENTITY = "entity";
    public static final int STATE_INIT = 0;
    public static final int STATE_ENTITY = 1;
    public static final int STATE_CLOSED = 2;
    public static final String VAL_SIZE = "size";
    public static final String VAL_POSITION = "position";
    public static final String VAL_ROTATION = "rotation";
    public static final String VAL_LAYER = "layer";
    public static final char CHAR_REFERENCE = '@';
    public static final char CHAR_EXPRESSION = '$';
    public static final ArrayList<Character> SYMBOLS = new ArrayList<>();
    public static final String DEFAULT_PACKAGE = "com.moka.components.";

    private Evaluator evaluator;
    private String entityName;
    private Context context;
    private SAXParser parser;
    private Entity entity;
    private int state;

    /**
     * Defines special symbols.
     */
    static
    {
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
            if (state == STATE_INIT && qName.equals(TAG_ENTITY))
            {
                state = STATE_ENTITY;
                entity = context.newEntity(entityName, readLayer(attributes));
                setTransformValues(entity.getTransform(), attributes);
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
            if (qName.equals(TAG_ENTITY))
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
        this.context = context;

        // the parser needs to be closed in order to read a new file.
        state = STATE_CLOSED;

        // create the JEval evaluator.
        evaluator = new Evaluator();

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

    /**
     * Read and load an entity XML file.
     *
     * @param filePath path to the XML file.
     * @param name     name for the new entity.
     * @return the created entity.
     */
    public Entity read(String filePath, String name)
    {
        if (state != STATE_CLOSED)
        {
            throw new JMokaException("XmlEntityReader's state is not init.");
        }

        // first state is init.
        state = STATE_INIT;
        entity = null;
        entityName = name;

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
            // meaning that the have the XmlAttribute annotation.
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
     * For a component name, return the component class. This allows xml to just write "Sprite" instead of
     * com.moka.components.Sprite. This also considers the secondary path.
     *
     * @param componentName the component name.
     * @return the component class.
     */
    public Class<?> forComponent(String componentName)
    {
        // this will allow us to throw an exception if the component has a package
        // but we couldn't find it.
        boolean componentHasPackage = hasPackage(componentName);

        // append the package if needed.
        String name = componentHasPackage ? componentName : DEFAULT_PACKAGE + componentName;

        try
        {
            return Class.forName(name);
        }
        catch (ClassNotFoundException e)
        {
            // try to find the component in the secondary path.
            if (!componentHasPackage && context.getSecondaryPackage() != null)
            {
                // define the secondary path.
                name = context.getSecondaryPackage() + "." + componentName;

                // try to find it in the secondary package.
                try
                {
                    return Class.forName(name);
                }
                catch (ClassNotFoundException d)
                {
                    throw new JMokaException("Component class " + name + " not found.");
                }
            }
            else
            {
                throw new JMokaException("Component class " + name + " not found.");
            }
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
        XmlAttribute attribute = method.getAnnotation(XmlAttribute.class);
        String value = attributes.getValue(attribute.value());

        // save the component name for errors.
        String componentName = component.getClass().getSimpleName();

        // if the value is not present we can either ignore it or, if the XmlAttribute
        // specifies that is required, throw an exception.
        if (value == null)
        {
            if (attribute.required())
            {
                throw new JMokaException("Component " + componentName + " requires the attribute "
                        + attribute.value());
            }
            return;
        }

        // here the value is always something. We should always take the first parameter
        // type because these methods are supposed to have only one.
        Class<?> param = getParamFor(method);

        // create the needed casted object.
        Object casted = null;

        // test different cases, where the param can be a trigger, a prefab, or other thing.
        if (param.isAssignableFrom(Trigger.class))
        {
            // This magic piece of code is awesome.

            // get the generic types of the parameters
            Type[] type = method.getGenericParameterTypes();

            // get the parametrized type, that is, a type with parameters.
            ParameterizedType pType = (ParameterizedType) type[0];

            // get the real type parameter for the first generic type.
            Type metaType = pType.getActualTypeArguments()[0];

            // finally catch the class of that generic type.
            Class<?> metaClass = metaType.getClass();

            // get the trigger.
            casted = Trigger.getStaticTrigger(value, metaClass);
        }
        else if (param.isAssignableFrom(Prefab.class))
        {
            casted = context.newPrefab(value);
        }
        else
        {
            // test the value and cast it to the parameter's class.
            casted = getTestedValue(param, value);
        }

        try
        {
            method.invoke(component, casted);
        }
        catch (IllegalAccessException e)
        {
            throw new JMokaException(String.format("Method %s for component %s is inaccessible",
                    method.getName(), componentName));
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
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
     * Test the value to match the param class, also if the param type is a Entity, this will find
     * that entity for you. If the value is a reference to a resource value, that value will be
     * searched and delivered, and finally, if the value is an expression, this will attempt to
     * resolve it.
     * <p/>
     * TODO: catch components with EntityName.ComponentClass.
     *
     * @param param the parameter class
     * @param value the value that will be tested.
     * @param <T>   the generic type of the parameter.
     * @return the resulting value, one of: int, float, double, boolean, String or Entity.
     */
    @SuppressWarnings("unchecked")
    public <T> T getTestedValue(Class<T> param, String value)
    {
        if (value.charAt(0) == CHAR_REFERENCE)
        {
            String resource = value.substring(1);
            Object result = null;

            if (param == int.class)
            {
                result = ((Number) context.getResources().get(resource)).intValue();
            }
            else if (param == float.class)
            {
                result = ((Number) context.getResources().get(resource)).floatValue();
            }
            else if (param == double.class)
            {
                result = ((Number) context.getResources().get(resource)).doubleValue();
            }
            else if (param == boolean.class)
            {
                result = context.getResources().getBoolean(resource);
            }
            else if (param == String.class)
            {
                result = context.getResources().getString(resource);
            }
            else if (param == Entity.class)
            {
                result = context.findEntity(context.getResources().getString(resource));
            }

            return (T) result;
        }
        else if (value.charAt(0) == CHAR_EXPRESSION)
        {
            Object result = null;
            String expression = replaceReferences(value.substring(2, value.length() - 1));
            String expValue = null;

            try
            {
                expValue = evaluator.evaluate(expression);
            }
            catch (EvaluationException e)
            {
                throw new JMokaException("[JEval] " + e.toString());
            }

            if (param == int.class)
            {
                result = Integer.parseInt(expValue);
            }
            else if (param == float.class)
            {
                result = Float.parseFloat(expValue);
            }
            else if (param == double.class)
            {
                result = Double.parseDouble(expValue);
            }
            else if (param == boolean.class)
            {
                result = Boolean.parseBoolean(expValue);
            }

            return (T) result;
        }
        else
        {
            Object result = null;

            if (param == int.class)
            {
                result = Integer.parseInt(value);
            }
            else if (param == float.class)
            {
                result = Float.parseFloat(value);
            }
            else if (param == double.class)
            {
                result = Double.parseDouble(value);
            }
            else if (param == boolean.class)
            {
                result = Boolean.parseBoolean(value);
            }
            else if (param == String.class)
            {
                result = value;
            }
            else if (param == Entity.class)
            {
                result = context.findEntity(value);
            }

            return (T) result;
        }
    }

    /**
     * Set the transform values of the given {@link com.moka.core.Entity}, casting them from
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
            Vector2f position = readPositionValues(attributes);
            transform.setPosition(new Vector2f(position.x, position.y));
        }

        if (attributes.getValue(VAL_ROTATION) != null)
        {
            float rotation = readRotation(attributes);
            transform.setRotationDeg(rotation);
        }

        if (attributes.getValue(VAL_SIZE) != null)
        {
            Vector2f size = readSizeValues(attributes);
            transform.setSize(size);
        }
    }

    /**
     * Reads the position given an attributes object.
     *
     * @param attributes the attributes objects of an entity.
     * @return the position as a 2D vector.
     */
    public Vector2f readPositionValues(Attributes attributes)
    {
        String[] params = attributes.getValue(VAL_POSITION).split(" *, *");
        float x = getTestedValue(float.class, params[0]);
        float y = getTestedValue(float.class, params[1]);
        return new Vector2f(x, y);
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
    public Vector2f readSizeValues(Attributes attributes)
    {
        String[] params = attributes.getValue(VAL_SIZE).split(" *, *");
        float x = getTestedValue(float.class, params[0]);
        float y = getTestedValue(float.class, params[1]);
        return new Vector2f(x, y);
    }

    /**
     * Obtains the parameter class for a given method, since the engine only allows to receive
     * one parameter in an qualified method, if the method has more than one parameter, the program
     * will crash so the client can fix this.
     *
     * @param method the method.
     * @return the parameter's class.
     */
    Class<?> getParamFor(Method method)
    {
        Class<?>[] params = method.getParameterTypes();

        // so, if the quantity of parameters is not equal to one, there's an error in the
        // definition of the method.
        if (params.length != 1)
        {
            throw new JMokaException(String.format("Method %s for component %s has more or less" +
                            "than one parameter, this is not allowed.", method.getName(),
                    method.getDeclaringClass().getName()));
        }

        return params[0];
    }

    /**
     * Gets the qualified methods of a given class, meaning, all methods that have an
     * {@link XmlAttribute} annotation in it.
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
            XmlAttribute attribute = method.getAnnotation(XmlAttribute.class);

            // if the attribute annotation is not null, then the method is Xml Qualified,
            // so it can be added to the resulting list.
            if (attribute != null)
            {
                qualified.add(method);
            }
        }

        return qualified;
    }

    /**
     * Replaces all reference with string values in an expression in order to evaluate it latter.
     *
     * @param expression the expression to be fixed.
     * @return the replaced expression.
     */
    private String replaceReferences(String expression)
    {
        StringBuilder curReference = new StringBuilder();
        HashSet<String> references = new HashSet<>();
        boolean rRef = false;

        for (int i = 0; i < expression.length(); i++)
        {
            char c = expression.charAt(i);

            if (rRef)
            {
                if (curReference.length() == 0)
                {
                    if (Character.isLetter(c))
                    {
                        curReference.append(c);
                    }
                    else
                    {
                        throw new JMokaException("Malformed reference.");
                    }
                }
                else
                {
                    if (SYMBOLS.contains(c))
                    {
                        rRef = false;
                        references.add(curReference.toString());
                        curReference.setLength(0);
                    }
                    else
                    {
                        curReference.append(c);
                    }
                }
            }
            else
            {
                if (c == CHAR_REFERENCE)
                {
                    rRef = true;
                }
            }
        }

        // if a reference was the last thing of the expression then append it to the reference list.
        if (rRef)
        {
            references.add(curReference.toString());
        }

        StringBuilder result = new StringBuilder(expression);
        for (String reference : references)
        {
            replaceAll(result, CHAR_REFERENCE + reference, context.getResources().get(reference).toString());
        }

        return result.toString();
    }

    /**
     * Simply replaces all occurrences in a string builder. Just a helper method.
     *
     * @param builder the string builder.
     * @param from    the string that will be searched.
     * @param to      the string that will replace the searched one.
     * @return the new string builder object.
     */
    private StringBuilder replaceAll(StringBuilder builder, String from, String to)
    {
        int index = builder.indexOf(from);
        while (index != -1)
        {
            builder.replace(index, index + from.length(), to);
            index += to.length();
            index = builder.indexOf(from, index);
        }

        return builder;
    }

    /**
     * Checks if for a given component class is there any specified package route declared.
     */
    private boolean hasPackage(String componentClass)
    {
        return componentClass.split("\\.").length != 1;
    }
}
