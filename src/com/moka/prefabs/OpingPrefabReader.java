package com.moka.prefabs;

import com.moka.core.ComponentAttribute;
import com.moka.core.Moka;
import com.moka.core.NameManager;
import com.moka.core.Prefab;
import com.moka.core.entity.Component;
import com.moka.math.Vector2;
import com.moka.triggers.Trigger;
import com.moka.triggers.TriggerPromise;
import com.moka.utils.JMokaException;
import com.shelodev.oping.OpingParser;
import com.shelodev.oping.structure.Branch;
import com.shelodev.oping.structure.Leaf;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;

public class OpingPrefabReader extends PrefabReader
{
    protected static final ArrayList<Character> SYMBOLS = new ArrayList<>();
    private static final OpingParser PARSER = new OpingParser();
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

    @Override
    public Prefab newPrefab(String filePath)
    {
        Prefab.PreComponents componentAttrs = new Prefab.PreComponents();
        Prefab prefab = new Prefab(componentAttrs);

        try
        {
            ArrayList<Branch> branches = PARSER.forestParsing(filePath);

            if (branches.size() != 1)
            {
                throw new JMokaException("The prefab needs to have just one tree.");
            }

            // process the prefab.
            takeBranch(branches.get(0), prefab);
        }
        catch (IOException e)
        {
            throw new JMokaException("Error while reading the prefab file.");
        }

        return prefab;
    }

    private void parseAndSetAttributes(Branch branch, Prefab prefab)
    {
        // get prefab attributes.
        Leaf position = branch.getLeaf("Position");
        Leaf rotation = branch.getLeaf("Rotation");
        Leaf size = branch.getLeaf("Size");
        Leaf layer = branch.getLeaf("Layer");
        Leaf group = branch.getLeaf("Group");

        // take care of each attribute if it exist.
        if (position != null)
        {
            prefab.setPosition(forPosition(position));
        }

        if (rotation != null)
        {
            prefab.setRotation(forRotation(rotation));
        }

        if (size != null)
        {
            prefab.setSize(forSize(size));
        }

        if (layer != null)
        {
            prefab.setLayer(forLayer(layer));
        }

        if (group != null)
        {
            prefab.setGroup(forGroup(group));
        }
    }

    private String forGroup(Leaf group)
    {
        if (group.size() != 1)
        {
            throw new JMokaException("The group attribute is just one string value.");
        }

        return group.getValue(0);
    }

    private Vector2 forSize(Leaf size)
    {
        if (size.size() != 2)
        {
            throw new JMokaException("The size attribute should have 2 values.");
        }

        float width = Float.parseFloat(size.getValue(0));
        float height = Float.parseFloat(size.getValue(1));

        return new Vector2(width, height);
    }

    private int forLayer(Leaf layer)
    {
        if (layer.size() != 1)
        {
            throw new JMokaException("The layer attribute is just one integer value.");
        }

        return Integer.parseInt(layer.getValue(0));
    }

    private float forRotation(Leaf rotation)
    {

        if (rotation.size() != 1)
        {
            throw new JMokaException("The rotation attribute is just one float value.");
        }

        return (float) Math.toRadians(Float.parseFloat(rotation.getValue(0)));
    }

    private Vector2 forPosition(Leaf position)
    {
        if (position.size() != 2)
        {
            throw new JMokaException("The position attribute should have 2 values.");
        }

        float x = Float.parseFloat(position.getValue(0));
        float y = Float.parseFloat(position.getValue(1));

        return new Vector2(x, y);
    }

    private void takeBranch(Branch branch, Prefab prefab)
    {
        parseAndSetAttributes(branch, prefab);

        for (Branch componentBranch : branch.getBranches())
        {
            String namespace = componentBranch.getNamespace();
            String name = componentBranch.getName();

            if (namespace == null)
            {
                namespace = NameManager.DEFAULT_NAMESPACE;
            }

            Class<? extends Component> componentClass = Moka.getNameManager().findClass(namespace, name);
            Prefab.ComponentAttrs attrs = new Prefab.ComponentAttrs();

            ArrayList<Method> methods = getQualifiedMethods(componentClass);

            for (Method method : methods)
            {
                ComponentAttribute attribute = method.getAnnotation(ComponentAttribute.class);
                Leaf leaf = componentBranch.getLeaf(attribute.value());

                if (leaf == null)
                {
                    continue;
                }

                String value = leaf.getValue(0);
                if (validateAttribute(attribute, value, componentClass.getSimpleName()))
                {
                    Class<?> param = getParamFor(method);

                    Object casted;

                    // we have to take different paths with different types, as we have to act differently when
                    // instantiating.
                    if (param.isAssignableFrom(Trigger.class))
                    {
                        casted = new TriggerPromise(Trigger.getStaticTriggerClass(value,
                                getTriggerGenericClass(method)));
                    }
                    else if (param.isEnum())
                    {
                        casted = castEnumType(param, value);
                    }
                    else
                    {
                        casted = getTestedValue(param, value);
                    }

                    attrs.addMethodValue(method, casted);
                }
            }

            prefab.getComponents().put(componentClass, attrs);
        }
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
            Object result = null;
//            String resource = value.substring(1);
//
//            if (param == int.class || param == Integer.class)
//            {
//                result = ((Number) Moka.getResources().getResources().get(resource)).intValue();
//            }
//            else if (param == float.class || param == Float.class)
//            {
//                result = ((Number) Moka.getResources().getResources().get(resource)).floatValue();
//            }
//            else if (param == double.class || param == Double.class)
//            {
//                result = ((Number) Moka.getResources().getResources().get(resource)).doubleValue();
//            }
//            else if (param == boolean.class || param == Boolean.class)
//            {
//                result = Moka.getResources().getResources().getBoolean(resource);
//            }
//            else if (param == String.class)
//            {
//                result = Moka.getResources().getResources().getString(resource);
//            }
//            else if (param == Prefab.class)
//            {
//                // TODO: this will change so blank for now.
//            }

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

    private boolean validateAttribute(ComponentAttribute attribute, String value, String simpleName)
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

    private ArrayList<Method> getQualifiedMethods(Class<? extends Component> componentClass)
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

    public char getReferenceChar()
    {
        return '@';
    }

    public char getExpressionChar()
    {
        return '$';
    }
}
