package com.moka.core.readers.xml;

import com.moka.core.contexts.Context;
import com.moka.core.entity.Entity;
import com.moka.core.readers.SceneReader;
import com.moka.utils.CoreUtil;
import com.moka.utils.JMokaException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XmlSceneReader implements SceneReader
{
    public static final String TAG_INCLUDE = "include";
    public static final String TAG_ENTITY = "entity";
    public static final String TAG_ROOT = "scene";
    public static final String KEY_NAME = "name";
    public static final String KEY_PATH = "path";
    public static final String KEY_GROUP = "group";

    private XmlEntityReader entityReader;
    private String baseDirectoryPath;
    private String currentFilePath;
    private Entity currentEntity;
    private SAXParser parser;
    private Context context;

    @Override
    public void read(String filePath)
    {
        try
        {
            InputStream stream = new FileInputStream(filePath);
            currentFilePath = filePath;
            // TODO: use the base directory to find resting files referenced in the scene.
            baseDirectoryPath = CoreUtil.getBaseDirectory(filePath);
            parser.parse(stream, new Handler());
            entityReader.resolvePendingTransactions();
        }
        catch (SAXException | IOException e)
        {
            throw new JMokaException(e.getMessage());
        }
    }

    private class Handler extends DefaultHandler
    {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
            if (currentEntity == null)
            {
                // if the currentEntity is null then we should read "Entity" next,
                // if that is not the case, then the XML is malformed.
                if (qName.equals(TAG_ENTITY))
                {
                    // get and set the name for that entity.
                    String name = attributes.getValue(KEY_NAME);
                    String group = attributes.getValue(KEY_GROUP);
                    currentEntity = context.newEntity(name, entityReader.readLayer(attributes));
                    currentEntity.setGroup(group);

                    // set transform position, rotation and scale.
                    entityReader.setTransformValues(currentEntity.getTransform(), attributes);
                }
                else if (qName.equals(TAG_INCLUDE))
                {
                    // with a include tag we should create an entity, put the name that it should have and
                    // override possible transform properties.
                    String path = attributes.getValue(KEY_PATH);
                    String name = attributes.getValue(KEY_NAME);
                    String group = attributes.getValue(KEY_GROUP);

                    Entity entity = entityReader.read(path, name);
                    entityReader.setTransformValues(entity.getTransform(), attributes);

                    if (group != null)
                    {
                        entity.setGroup(group);
                    }
                }
                else if (!qName.equals(TAG_ROOT))
                {
                    throw new JMokaException("XML: " + currentFilePath + " is malformed.");
                }
            }
            else
            {
                entityReader.readComponent(currentEntity, qName, attributes);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException
        {
            if (qName.equals(TAG_ENTITY))
            {
                currentEntity = null;
            }
        }

        @Override
        public void endDocument() throws SAXException
        {
            currentEntity = null;
            currentFilePath = null;
        }
    }

    public XmlSceneReader(Context context)
    {
        this.context = context;

        entityReader = new XmlEntityReader(context);

        try
        {
            parser = SAXParserFactory.newInstance().newSAXParser();
        }
        catch (ParserConfigurationException | SAXException e)
        {
            throw new JMokaException("SAXParser creation error.");
        }
    }

    public XmlEntityReader getEntityReader()
    {
        return entityReader;
    }
}
