package com.moka.core.sync;

import com.moka.core.readers.ComponentAttribute;
import com.moka.utils.JMokaException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ComponentSchemeWriter
{
    private static final String TYPE_LINE =
            "      <xs:element name=\"%s\" minOccurs=\"0\" maxOccurs=\"1\">" +
            "<xs:complexType>\n";

    private static final String ATTRIBUTE_LINE =
            "        <xs:attribute name=\"%s\" type=\"xs:%s\" use=\"%s\" />\n";

    private static final String ATTRIBUTE_LINE_ENUM =
            "        <xs:attribute name=\"%s\" use=\"%s\"><xs:simpleType>\n";

    private static final String ENUM_VALUE =
            "            <xs:enumeration value=\"%s\" />\n";

    public static ComponentDescriptor write(Class<?> component)
    {
        final StringBuilder xmlResult = new StringBuilder();
        final JSONObject jsonObject = new JSONObject();

        xmlResult.append(String.format(TYPE_LINE, component.getSimpleName()));

        // get all methods that have the ComponentAttribute annotation.
        List<Method> attrMethods = findAttributeMethods(component);

        // iterate over all attribute methods.
        for (Method attrMethod : attrMethods)
        {
            try
            {
                analyzeMethod(xmlResult, jsonObject, attrMethod);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        xmlResult.append("      </xs:complexType></xs:element>\n\n");

        return new ComponentDescriptor(xmlResult, jsonObject);
    }

    public static void analyzeMethod(StringBuilder xmlResult, JSONObject jsonResult, Method attrMethod)
            throws JSONException
    {
        Class<?>[] params = attrMethod.getParameterTypes();

        if (params.length != 1)
        {
            throw new JMokaException("Method " + attrMethod.getName() + " has more or less than one parameter.");
        }

        Class<?> param = params[0];

        ComponentAttribute attribute = attrMethod.getAnnotation(ComponentAttribute.class);

        String name = attribute.value();
        String required = attribute.required() ? "required" : "optional";

        appendXmlString(param, xmlResult, name, required);
        appendJsonString(param, jsonResult, name, attribute.required());
    }

    private static void appendJsonString(Class<?> param, JSONObject jsonComponent, String attrName, Boolean required)
            throws JSONException
    {
        JSONObject jsonAttribute = new JSONObject();

        String type = getTypeOf(param);
        jsonAttribute.put("required", required);

        // add enum values if needed.
        if (param.isEnum())
        {
            Object[] constants = param.getEnumConstants();

            JSONArray array = new JSONArray();

            for (Object constant : constants)
            {
                array.put(constant.toString());
            }

            jsonAttribute.put("values", array);

            jsonAttribute.put("type", "enum");
        }
        else
        {
            jsonAttribute.put("type", type);
        }

        jsonComponent.put(attrName, jsonAttribute);
    }

    private static void appendXmlString(Class<?> param, StringBuilder result, String name, String required)
    {
        // enum is very special so it has to be done separately.
        if (param.isEnum())
        {
            Object[] constants = param.getEnumConstants();

            result.append(String.format(ATTRIBUTE_LINE_ENUM, name, required));
            result.append("          <xs:restriction base=\"xs:string\">\n");

            for (Object constant : constants)
            {
                result.append(String.format(ENUM_VALUE, constant.toString()));
            }

            result.append("          </xs:restriction>\n");
            result.append("        </xs:simpleType></xs:attribute>\n");
        }
        else
        {
            String type = getTypeOf(param);
            result.append(String.format(ATTRIBUTE_LINE, name, type, required));
        }
    }

    private static String getTypeOf(Class<?> param)
    {
        String type = param.getSimpleName();
        type = type.equals("int") ? "integer" : type;
        type = type.equals("Trigger") ? "string" : type;
        type = type.equals("String") ? "string" : type;
        type = type.equals("Prefab") ? "string" : type;
        type = type.equals("Entity") ? "string" : type;
        return type;
    }

    private static List<Method> findAttributeMethods(Class<?> component)
    {
        List<Method> result = new ArrayList<>();

        Method[] methods = component.getMethods();

        for (Method method : methods)
        {
            ComponentAttribute annotation = method.getAnnotation(ComponentAttribute.class);

            if (annotation != null)
            {
                result.add(method);
            }
        }

        return result;
    }
}
