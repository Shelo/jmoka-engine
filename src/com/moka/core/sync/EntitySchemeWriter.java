package com.moka.core.sync;

import com.moka.components.*;
import com.moka.core.entity.Component;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EntitySchemeWriter
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
            "    </xs:sequence>\n\n" +
            "    <xs:attribute name=\"group\" type=\"xs:string\" use=\"optional\" />\n" +
            "    <xs:attribute name=\"layer\" type=\"xs:int\" use=\"optional\" default=\"0\" />\n" +
            "    <xs:attribute name=\"position\" type=\"xs:string\" use=\"required\" default=\"0, 0\" />\n" +
            "    <xs:attribute name=\"rotation\" type=\"xs:float\" use=\"required\" default=\"0\" />\n" +
            "    <xs:attribute name=\"size\" type=\"xs:string\" use=\"optional\" default=\"100, 100\" />\n" +
            "  </xs:complexType>\n" +
            "</xs:schema>";

    private static List<Class<? extends Component>> components = new ArrayList<>();

    static {
        register(AABBCollider.class);
        register(Bullet.class);
        register(Camera.class);
        register(CircleCollider.class);
        register(Controllable.class);
        register(Interval.class);
        register(LookAt.class);
        register(SatCollider.class);
        register(Shooting.class);
        register(Sprite.class);
    }

    public static void register(Class<? extends Component> component)
    {
        components.add(component);
    }

    public static void render()
    {
        String xmlDestFile = "gen/xsd/entity_type.xsd";
        String jsonDestFile = "gen/components.json";

        final StringBuilder source = new StringBuilder();
        final JSONObject jsonObject = new JSONObject();

        // add the first part (the header) to the xml source text.
        source.append(HEADER + "\n");

        for (Class<?> component : components)
        {
            ComponentDescriptor descriptor = ComponentSchemeWriter.write(component);
            source.append(descriptor.getStringBuilder().toString());

            try
            {
                jsonObject.put(component.getSimpleName(), descriptor.getJsonObject());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        source.append(FOOTER);

        try
        {
            PrintWriter writer = new PrintWriter(xmlDestFile);
            writer.write(source.toString());
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            FileWriter jsonFile = new FileWriter(jsonDestFile);
            jsonFile.write(jsonObject.toString());
            jsonFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
