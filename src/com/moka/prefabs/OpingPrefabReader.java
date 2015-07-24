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
    private static final OpingParser PARSER = new OpingParser();

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
            throw new JMokaException("[Prefab Error] " + e.getMessage());
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

        return getTestedValue(String.class, group.getValue(0));
    }

    private Vector2 forSize(Leaf size)
    {
        if (size.size() != 2)
        {
            throw new JMokaException("The size attribute should have 2 values.");
        }

        float width = getTestedValue(Float.class, size.getValue(0));
        float height = getTestedValue(Float.class, size.getValue(1));

        return new Vector2(width, height);
    }

    private int forLayer(Leaf layer)
    {
        if (layer.size() != 1)
        {
            throw new JMokaException("The layer attribute is just one integer value.");
        }

        return getTestedValue(Integer.class, layer.getValue(0));
    }

    private float forRotation(Leaf rotation)
    {

        if (rotation.size() != 1)
        {
            throw new JMokaException("The rotation attribute is just one float value.");
        }

        Float value = getTestedValue(Float.class, rotation.getValue(0));
        return (float) Math.toRadians(value);
    }

    private Vector2 forPosition(Leaf position)
    {
        if (position.size() != 2)
        {
            throw new JMokaException("The position attribute should have 2 values.");
        }

        float x = getTestedValue(Float.class, position.getValue(0));
        float y = getTestedValue(Float.class, position.getValue(1));

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
}
