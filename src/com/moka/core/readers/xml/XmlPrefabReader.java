package com.moka.core.readers.xml;

import com.moka.core.contexts.Context;
import com.moka.core.Prefab;
import com.moka.core.readers.ComponentAttribute;
import com.moka.core.readers.PrefabReader;
import com.moka.math.Vector2;
import com.moka.triggers.Trigger;
import com.moka.triggers.TriggerPrefab;
import com.moka.utils.JMokaException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class XmlPrefabReader extends PrefabReader
{
    private XmlEntityReader entityReader;
    private SAXParser parser;
    private Context context;

    private class Handler extends DefaultHandler
    {
        private static final String TAG_ENTITY = "entity";

        Prefab.PreComponents preComponents;
        private Prefab prefab;

        public Handler(Prefab prefab, Prefab.PreComponents preComponents)
        {
            this.preComponents = preComponents;
            this.prefab = prefab;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException
        {
            if (qName.equals(TAG_ENTITY))
            {
                if (attributes.getValue(XmlEntityReader.VAL_POSITION) != null)
                {
                    Vector2 position = entityReader.readPositionValues(attributes);
                    prefab.setPosition(position);
                }

                if (attributes.getValue(XmlEntityReader.VAL_LAYER) != null)
                {
                    int layer = entityReader.readLayer(attributes);
                    prefab.setLayer(layer);
                }

                if (attributes.getValue(XmlEntityReader.VAL_ROTATION) != null)
                {
                    float rotation = entityReader.readRotation(attributes);
                    prefab.setRotation(rotation);
                }

                if (attributes.getValue(XmlEntityReader.VAL_SIZE) != null)
                {
                    Vector2 size = entityReader.readSizeValues(attributes);
                    prefab.setSize(size);
                }
            }
            else
            {
                Class<?> componentClass = entityReader.forComponent(qName);
                Prefab.ComponentAttrs attrs = new Prefab.ComponentAttrs();

                ArrayList<Method> qMethods = entityReader.getQualifiedMethods(componentClass);

                for (Method method : qMethods)
                {
                    ComponentAttribute attribute = method.getAnnotation(ComponentAttribute.class);
                    String value = attributes.getValue(attribute.value());

                    if (entityReader.validateAttribute(attribute, value, componentClass.getSimpleName()))
                    {
                        Class<?> param = entityReader.getParamFor(method);

                        Object casted = null;

                        // we have to take different paths with different types, as we have to act differently when
                        // instantiating.
                        if (param.isAssignableFrom(Trigger.class))
                        {
                            casted = new TriggerPrefab(Trigger.getStaticTriggerClass(value,
                                    entityReader.getTriggerGenericClass(method)));
                        }
                        else if (param.isEnum())
                        {
                            casted = entityReader.castEnumType(param, value);
                        }
                        else
                        {
                            casted = entityReader.getTestedValue(param, value);
                        }

                        attrs.addMethodValue(method, casted);
                    }
                }

                preComponents.put(componentClass, attrs);
            }
        }
    }

    public XmlPrefabReader(XmlEntityReader entityReader, Context context)
    {
        try
        {
            parser = SAXParserFactory.newInstance().newSAXParser();
        }
        catch (ParserConfigurationException | SAXException e)
        {
            throw new JMokaException("Error SAXParser: " + e.toString());
        }

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
    @Override
    public Prefab newPrefab(String filePath)
    {
        Prefab.PreComponents componentAttrs = new Prefab.PreComponents();
        Prefab prefab = new Prefab(context, componentAttrs);
        Handler handler = new Handler(prefab, componentAttrs);

        try
        {
            parser.parse(filePath, handler);
        }
        catch (SAXException | IOException e)
        {
            throw new JMokaException("SAX parser exception: " + e.toString());
        }

        return prefab;
    }
}
