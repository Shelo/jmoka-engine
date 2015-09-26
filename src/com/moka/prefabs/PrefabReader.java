package com.moka.prefabs;

import com.moka.scene.entity.ComponentAttribute;
import com.moka.core.Moka;
import com.moka.scene.entity.Component;
import com.moka.utils.JMokaException;
import com.shelodev.oping.structure.Leaf;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

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
     * that getEntity for you. If the value is a reference to a resource value, that value will be
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
            Object result;

            // in case that this is a resources (marked with the resource char), then grab the resource name
            // and find it in the resources specified by the user.
            String resource = value.substring(1);
            String[] parts = resource.split("\\.");

            if (parts.length < 2)
            {
                throw new JMokaException("The resource name must have at least two values.");
            }

            result = Moka.getResources().findResource(resource);

            if (param == int.class || param == Integer.class)
            {
                result = ((Number) result).intValue();
            }
            else if (param == float.class || param == Float.class)
            {
                result = ((Number) result).floatValue();
            }
            else if (param == double.class || param == Double.class)
            {
                result = ((Number) result).doubleValue();
            }

            return (T) result;
        }
        else
        {
            Object result = null;

            try
            {
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
            }
            catch (NumberFormatException eFormat)
            {
                // if the value is not parsable, then try to evaluate it.
                String expression = replaceReferences(value);
                String expValue;

                try
                {
                    expValue = evaluator.evaluate(expression);
                }
                catch (EvaluationException e)
                {
                    throw new JMokaException("[JEval] " + e.toString());
                }

                result = getTestedValue(param, expValue);
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
                    if (Character.isLetter(c) || c == '.')
                    {
                        curReference.append(c);
                    }
                    else
                    {
                        throw new JMokaException("Malformed reference in: " + expression);
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
                if (Character.isLetter(c))
                {
                    rRef = true;
                    curReference.append(c);
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
            replaceAll(result, reference, String.valueOf(Moka.getResources().findResource(reference)));
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
    public Class<?>[] getParamsFor(Method method)
    {
        return method.getParameterTypes();
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

    public boolean validateAttribute(ComponentAttribute attribute, Leaf leaf, Method method, String simpleName)
    {
        if (leaf == null)
        {
            if (attribute.required())
            {
                throw new JMokaException("Component " + simpleName + " requires the '" + attribute.value()
                        + "' attribute.");
            }

            return false;
        }
        else if (leaf.size() != method.getParameterCount())
        {
            // check if the type is an array
            Class<?>[] params = method.getParameterTypes();

            if (params.length != 0 & params[0].isArray())
            {
                return true;
            }

            throw new JMokaException("The attribute " + attribute.value() + " needs " + method.getParameterCount()
                + " values.");
        }

        return true;
    }

    /**
     * Returns a list of all qualified methods (methods that have the ComponentAttribute annotation) of
     * a given component class.
     *
     * @param componentClass    the component class to inspect.
     * @return                  qualified methods of the component.
     */
    public static ArrayList<Method> getQualifiedMethods(Class<? extends Component> componentClass)
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
}
