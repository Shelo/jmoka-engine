package com.moka.core.readers;

import com.moka.core.Prefab;
import com.moka.core.contexts.Context;
import com.moka.core.entity.Component;
import com.moka.core.entity.Entity;
import com.moka.triggers.Trigger;
import com.moka.utils.JMokaException;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class EntityReader
{
    public static final String DEFAULT_PACKAGE = "com.moka.components.";

    protected static final ArrayList<Character> SYMBOLS = new ArrayList<>();

    protected List<PendingTransaction> pendingTransactions;
    protected Context context;
    private Evaluator evaluator;

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

    public EntityReader(Context context)
    {
        this.context = context;
        this.pendingTransactions = new ArrayList<>();
        this.evaluator = new Evaluator();
    }

    /**
     * Read and load an entity from file.
     *
     * @param filePath      path to the entity file.
     * @param entityName    name for the new entity.
     * @return the created entity.
     */
    public abstract Entity read(String filePath, String entityName);
    protected abstract char getExpressionChar();
    protected abstract char getReferenceChar();

    public void resolvePendingTransactions()
    {
        for (int i = pendingTransactions.size() - 1; i >= 0; i--)
        {
            PendingTransaction pendingTransaction = pendingTransactions.get(i);
            invokeMethodOnComponent(pendingTransaction.getComponent(), pendingTransaction.getMethod(),
                    pendingTransaction.getValue());
            pendingTransactions.remove(i);
        }
    }

    public void invokeMethodOnComponent(Component component, Method method, String value)
    {
        // here the value is always something. We should always take the first parameter
        // type because these methods are supposed to have only one.
        Class<?> param = getParamFor(method);

        // create the needed casted object.
        Object casted;

        // test different cases, where the param can be a trigger, a prefab, or other thing.

        if (param.isAssignableFrom(Trigger.class))
        {
            if (value.isEmpty())
            {
                throw new JMokaException(component.getClass().getSimpleName() + "@" + component.entity().getName()
                        + "'s trigger is empty.");
            }

            // get a new instance for the trigger.
            casted = Trigger.getStaticTrigger(value, getTriggerGenericClass(method));
        }
        // in case the param type is an enum.
        else if (param.isEnum())
        {
            if (value.isEmpty())
            {
                throw new JMokaException(param.getDeclaringClass().getSimpleName() + "'s enum option cannot be blank.");
            }

            casted = castEnumType(param, value);
        }
        else
        {
            // test the value and cast it to the parameter's class.
            casted = getTestedValue(param, value);

            // this is a very special case. Here the entity we tried to reference doesn't exists yet, but
            // can exist in the future, so we will save this state to analyze it later.
            if (casted == null && param.isAssignableFrom(Entity.class))
            {
                pendingTransactions.add(new PendingTransaction(component, method, value));
                return;
            }
        }

        try
        {
            method.invoke(component, casted);
        }
        catch (IllegalAccessException e)
        {
            throw new JMokaException(String.format("Method %s for component %s is inaccessible",
                    method.getName(), component.getClass().getSimpleName()));
        }
        catch (InvocationTargetException e)
        {
            throw new JMokaException(String.format("Method %s for component %s cannot be called: %s",
                    method.getName(), component.getClass().getSimpleName(), e.getMessage()));
        }
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
            String resource = value.substring(1);
            Object result = null;

            if (param == int.class || param == Integer.class)
            {
                result = ((Number) context.getResources().get(resource)).intValue();
            }
            else if (param == float.class || param == Float.class)
            {
                result = ((Number) context.getResources().get(resource)).floatValue();
            }
            else if (param == double.class || param == Double.class)
            {
                result = ((Number) context.getResources().get(resource)).doubleValue();
            }
            else if (param == boolean.class || param == Boolean.class)
            {
                result = context.getResources().getBoolean(resource);
            }
            else if (param == String.class)
            {
                result = context.getResources().getString(resource);
            }
            else if (param == Entity.class)
            {
                try
                {
                    result = context.findEntity(value);
                }
                // if the entity is not found, we'll get an exception, but rather than quit,
                // we will mark this one as a pending transaction.
                catch (JMokaException e)
                {
                    return null;
                }
            }
            else if (param == Prefab.class)
            {
                result = context.newPrefab(context.getResources().getString(resource));
            }

            return (T) result;
        }
        else if (value.charAt(0) == getExpressionChar())
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
            else if (param == Entity.class)
            {
                try
                {
                    result = context.findEntity(value);
                }
                // if the entity is not found, we'll get an exception, but rather than quit,
                // we will mark this one as a pending transaction.
                catch (JMokaException e)
                {
                    return null;
                }
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
            replaceAll(result, getReferenceChar() + reference, context.getResources().get(reference).toString());
        }

        return result.toString();
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
}
