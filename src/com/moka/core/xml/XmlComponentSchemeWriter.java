package com.moka.core.xml;

import com.moka.core.Component;
import com.moka.utils.JMokaException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class XmlComponentSchemeWriter
{
    private static final String TYPE_LINE = "      <xs:element name=\"%s\" minOccurs=\"0\" maxOccurs=\"1\">" +
            "<xs:complexType>\n";
    private static final String ATTRIBUTE_LINE = "        <xs:attribute name=\"%s\" type=\"xs:%s\" use=\"%s\" />\n";

    public static String write(Class<?> component)
    {
        final StringBuilder result = new StringBuilder();

        result.append(String.format(TYPE_LINE, component.getSimpleName()));

        // get all methods that have the XmlAttribute annotation.
        List<Method> attrMethods = findAttributeMethods(component);

        // iterate over all attribute methods.
        for (Method attrMethod : attrMethods)
        {
            Class<?>[] params = attrMethod.getParameterTypes();

            if (params.length != 1)
            {
                throw new JMokaException("Method " + attrMethod.getName() + " has more or less than one parameter.");
            }

            Class<?> param = params[0];

            XmlAttribute attribute = attrMethod.getAnnotation(XmlAttribute.class);

            String name = attribute.value();
            String required = attribute.required() ? "required" : "optional";

            String type = param.getSimpleName();
            type = type.equals("int") ? "integer" : type;
            type = type.equals("Trigger") ? "string" : type;
            type = type.equals("String") ? "string" : type;
            type = type.equals("Prefab") ? "string" : type;
            type = type.equals("Entity") ? "string" : type;

            result.append(String.format(ATTRIBUTE_LINE, name, type, required));
        }

        result.append("      </xs:complexType></xs:element>\n\n");

        return result.toString();
    }

    private static List<Method> findAttributeMethods(Class<?> component)
    {
        List<Method> result = new ArrayList<>();

        Method[] methods = component.getMethods();

        for (Method method : methods)
        {
            XmlAttribute annotation = method.getAnnotation(XmlAttribute.class);

            if (annotation != null)
            {
                result.add(method);
            }
        }

        return result;
    }
}
