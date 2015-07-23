package com.moka.prefabs;

import com.moka.core.ComponentAttribute;
import com.moka.core.Moka;
import com.moka.core.MokaResources;
import com.moka.core.Prefab;
import com.moka.core.entity.Component;
import com.moka.utils.JMokaException;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class PrefabReader
{
    protected static final ArrayList<Character> SYMBOLS = new ArrayList<>();

    private Evaluator evaluator =  new Evaluator();

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

    public abstract Prefab newPrefab(String filePath);

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
        if (value.charAt(0) == getReferenceChar())
        {
            // in case that this is a resources (marked with the resource char), then grab the resource name
            // and find it in the resources specified by the user.
            String resource = value.substring(1);
            String[] parts = resource.split("\\.");

            if (parts.length != 2)
            {
                throw new JMokaException("The resource name must have two values.");
            }

            String innerClass = parts[0];
            String name = parts[1];

            return (T) Moka.getResources().findResource(innerClass, name);
        }
        else if (value.charAt(0) == getExpressionChar())
        {
            String expression = replaceReferences(value.substring(2, value.length() - 1));
            String expValue;

            try
            {
                expValue = evaluator.evaluate(expression);
            }
            catch (EvaluationException e)
            {
                throw new JMokaException("[JEval] " + e.toString());
            }

            return getTestedValue(param, expValue);
        }
        else
        {
            Object result = null;

            if (param == int.class || param == Integer.class)
            {
                result = Integer.parseInt(value);
            }
            else if (param == float.class || param == Float.class)
            {
                result = Float.parseFloat(value);
            }
            else if (param == double.class || param == Double.class)
            {
                result = Double.parseDouble(value);
            }
            else if (param == boolean.class || param == Boolean.class)
            {
                result = Boolean.parseBoolean(value);
            }
            else if (param == String.class)
            {
                result = value;
            }

            return (T) result;
        }
    }

    /**
     * Replaces all reference with string values in an expression in order to evaluate it latter.
     *
     * @param expression the expression to be fixed.
     * @return the replaced expression.
     */
    public String replaceReferences(String expression)
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
                if (c == getReferenceChar())
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
            // TODO: change this.
//            replaceAll(result, getReferenceChar() + reference, Moka.getResources().get(reference).toString());
        }

        return result.toString();
    }

    public Object castEnumType(Class<?> param, String value)
    {
        Object[] constants = param.getEnumConstants();

        for (Object constant : constants)
        {
            if (constant.toString().equals(value))
            {
                return constant;
            }
        }

        // if we get here, the value given for the enum is simply not valid.
        throw new JMokaException("Enum " + param.getSimpleName() + " has no value " + value + ".");
    }

    /**
     * Obtains the parameter class for a given method, since the engine only allows to receive
     * one parameter in an qualified method, if the method has more than one parameter, the program
     * will crash so the client can fix this.
     *
     * @param method the method.
     * @return the parameter's class.
     */
    public Class<?> getParamFor(Method method)
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

    public Class<?> getTriggerGenericClass(Method method)
    {
        // This magic piece of code is awesome.

        // get the generic types of the parameters
        Type[] type = method.getGenericParameterTypes();

        // get the parametrized type, that is, a type with parameters.
        ParameterizedType pType = (ParameterizedType) type[0];

        // get the real type parameter for the first generic type.
        Type metaType = pType.getActualTypeArguments()[0];

        // finally return the class of that generic type.
        return metaType.getClass();
    }

    public boolean validateAttribute(ComponentAttribute attribute, String value, String simpleName)
    {
        if (value == null)
        {
            if (attribute.required())
            {
                throw new JMokaException("Component " + simpleName + " requires the '" + attribute.value()
                        + "' attribute.");
            }

            return false;
        }

        return true;
    }

    public ArrayList<Method> getQualifiedMethods(Class<? extends Component> componentClass)
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

    /**
     * Simply replaces all occurrences in a string builder. Just a helper method.
     *
     * @param builder the string builder.
     * @param from    the string that will be searched.
     * @param to      the string that will replace the searched one.
     * @return the new string builder object.
     */
    public StringBuilder replaceAll(StringBuilder builder, String from, String to)
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

    public char getReferenceChar()
    {
        return '@';
    }

    public char getExpressionChar()
    {
        return '$';
    }
}
