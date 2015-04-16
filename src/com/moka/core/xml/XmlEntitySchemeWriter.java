package com.moka.core.xml;

import com.moka.components.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class XmlEntitySchemeWriter
{
    private static final String HEADER =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<xs:schema\n" +
            "  targetNamespace=\"http://www.moka-dev.com/entity_type\"\n" +
            "  xmlns=\"http://www.moka-dev.com/entity_type\"\n" +
            "  xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\n" +
            "  elementFormDefault=\"qualified\" >\n" +
            "\n" +
            "  <!-- Define Entity's Type. -->\n" +
            "  <xs:complexType name=\"EntityType\">\n" +
            "    <xs:sequence minOccurs=\"0\" maxOccurs=\"unbounded\">\n\n" +
            "      <!-- Start Generated Code -->\n";

    private static final String FOOTER =
            "      <!-- End Generated Code -->\n\n" +
            "      <xs:any processContents=\"skip\" minOccurs=\"0\" maxOccurs=\"unbounded\" />\n" +
            "    </xs:sequence>\n" +
            "    <xs:attribute name=\"layer\" type=\"xs:int\" use=\"optional\" default=\"0\" />\n" +
            "    <xs:attribute name=\"position\" type=\"xs:string\" use=\"required\" default=\"0, 0\" />\n" +
            "    <xs:attribute name=\"rotation\" type=\"xs:float\" use=\"required\" default=\"0\" />\n" +
            "    <xs:attribute name=\"size\" type=\"xs:string\" use=\"optional\" default=\"100, 100\" />\n" +
            "  </xs:complexType>\n" +
            "</xs:schema>";

    private static List<Class<?>> components = new ArrayList<>();

    static {
        XmlEntitySchemeWriter.register(AABBCollider.class);
        XmlEntitySchemeWriter.register(Bullet.class);
        XmlEntitySchemeWriter.register(Camera.class);
        XmlEntitySchemeWriter.register(CircleCollider.class);
        XmlEntitySchemeWriter.register(Controllable.class);
        XmlEntitySchemeWriter.register(Interval.class);
        XmlEntitySchemeWriter.register(LookAt.class);
        XmlEntitySchemeWriter.register(SatCollider.class);
        XmlEntitySchemeWriter.register(Shooting.class);
        XmlEntitySchemeWriter.register(Sprite.class);
    }

    public static void register(Class<?> component)
    {
        components.add(component);
    }

    public static void render()
    {
        String destFile = "res/xsd/entity_type.xsd";

        StringBuilder source = new StringBuilder();
        source.append(HEADER + "\n");

        for (Class<?> component : components)
        {
            source.append(XmlComponentSchemeWriter.write(component));
        }

        source.append(FOOTER);

        File file = new File(destFile);

        try
        {
            PrintWriter writer = new PrintWriter(file);
            writer.write(source.toString());
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
