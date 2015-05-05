package com.moka.core.readers.xml;

import com.moka.core.Resources;
import com.moka.utils.JMokaException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class XmlResourcesReader
{
    private enum STATE
    {
        NONE,
        VALUES,
        STRINGS,
        INTEGERS,
        FLOATS,
        DOUBLES,
        BOOLEANS
    }

    private SAXParser parser;
    private Resources resources;

    public XmlResourcesReader(Resources resources)
    {
        this.resources = resources;

        try
        {
            parser = SAXParserFactory.newInstance().newSAXParser();
        }
        catch (ParserConfigurationException e)
        {
            throw new JMokaException("Unable to create SAXParser.");
        }
        catch (SAXException e)
        {
            throw new JMokaException("SAX Exception.");
        }
    }

    public void read(String filePath)
    {
        try
        {
            parser.parse(filePath, new Helper());
        }
        catch (SAXException e)
        {
            throw new JMokaException("SAX Exception.");
        }
        catch (IOException e)
        {
            throw new JMokaException("IOException.");
        }
    }

    private class Helper extends DefaultHandler
    {
        private static final String TAG_BOOLEAN = "boolean";
        private static final String TAG_INTEGER = "integer";
        private static final String TAG_VALUES = "values";
        private static final String TAG_STRING = "string";
        private static final String TAG_DOUBLE = "double";
        private static final String TAG_FLOAT = "float";
        private static final String TAG_RES = "res";
        private static final String KEY_NAME = "name";
        private static final String KEY_VALUE = "value";

        private STATE state = STATE.NONE;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
            switch (state)
            {
                case NONE:
                    if (qName.equals(TAG_VALUES))
                    {
                        state = STATE.VALUES;
                    }
                    else
                    {
                        throw new JMokaException("Malformed Values XML file.");
                    }
                    break;

                case VALUES:
                    switch (qName)
                    {
                        case TAG_STRING:
                            state = STATE.STRINGS;
                            break;
                        case TAG_INTEGER:
                            state = STATE.INTEGERS;
                            break;
                        case TAG_FLOAT:
                            state = STATE.FLOATS;
                            break;
                        case TAG_DOUBLE:
                            state = STATE.DOUBLES;
                            break;
                        case TAG_BOOLEAN:
                            state = STATE.BOOLEANS;
                            break;
                        default:
                            throw new JMokaException("Malformed Values XML file.");
                    }
                    break;

                case STRINGS:
                    if (qName.equals(TAG_RES))
                    {
                        handleString(attributes);
                    }
                    else
                    {
                        throw new JMokaException("Not a proper string resource.");
                    }
                    break;

                case INTEGERS:
                    if (qName.equals(TAG_RES))
                    {
                        handleInteger(attributes);
                    }
                    else
                    {
                        throw new JMokaException("Not a proper integer resource.");
                    }
                    break;

                case FLOATS:
                    if (qName.equals(TAG_RES))
                    {
                        handleFloat(attributes);
                    }
                    else
                    {
                        throw new JMokaException("Not a proper float resource.");
                    }
                    break;

                case DOUBLES:
                    if (qName.equals(TAG_RES))
                    {
                        handleDouble(attributes);
                    }
                    else
                    {
                        throw new JMokaException("Not a proper double resource.");
                    }
                    break;

                case BOOLEANS:
                    if (qName.equals(TAG_RES))
                    {
                        handleBoolean(attributes);
                    }
                    else
                    {
                        throw new JMokaException("Not a proper boolean resource.");
                    }
                    break;
            }
        }

        private void handleString(Attributes attributes)
        {
            String name = attributes.getValue(KEY_NAME);
            String value = attributes.getValue(KEY_VALUE);
            checkError(name, value, TAG_STRING);
            resources.addResource(name, value);
        }

        private void handleBoolean(Attributes attributes)
        {
            String name = attributes.getValue(KEY_NAME);
            String value = attributes.getValue(KEY_VALUE);
            checkError(name, value, TAG_STRING);
            resources.addResource(name, Boolean.parseBoolean(value));
        }

        private void handleDouble(Attributes attributes)
        {
            String name = attributes.getValue(KEY_NAME);
            String value = attributes.getValue(KEY_VALUE);
            checkError(name, value, TAG_STRING);
            resources.addResource(name, Double.parseDouble(value));
        }

        private void handleFloat(Attributes attributes)
        {
            String name = attributes.getValue(KEY_NAME);
            String value = attributes.getValue(KEY_VALUE);
            checkError(name, value, TAG_STRING);
            resources.addResource(name, Float.parseFloat(value));
        }

        private void handleInteger(Attributes attributes)
        {
            String name = attributes.getValue(KEY_NAME);
            String value = attributes.getValue(KEY_VALUE);
            checkError(name, value, TAG_STRING);
            resources.addResource(name, Integer.parseInt(value));
        }

        public void checkError(String name, String value, String resType)
        {
            if (name == null || value == null)
            {
                throw new JMokaException("Bad definition for some " + resType + " resource.");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException
        {
            if (qName.equals(TAG_RES))
            {
                return;
            }

            switch (state)
            {
                case NONE:
                    break;
                case VALUES:
                    break;
                case STRINGS:
                    state = STATE.VALUES;
                    break;
                case INTEGERS:
                    state = STATE.VALUES;
                    break;
                case FLOATS:
                    state = STATE.VALUES;
                    break;
                case DOUBLES:
                    state = STATE.VALUES;
                    break;
                case BOOLEANS:
                    state = STATE.VALUES;
                    break;
            }
        }
    }
}
