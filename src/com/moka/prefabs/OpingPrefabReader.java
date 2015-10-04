package com.moka.prefabs;

import com.moka.core.Moka;
import com.moka.core.NameManager;
import com.moka.math.Vector2;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.triggers.Trigger;
import com.moka.triggers.TriggerPromise;
import com.moka.utils.CoreUtil;
import com.moka.utils.JMokaException;
import com.shelodev.oping2.OpingParser;
import com.shelodev.oping2.OpingParserException;
import com.shelodev.oping2.structure.Branch;
import com.shelodev.oping2.structure.Leaf;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
            Branch branch = PARSER.parse(CoreUtil.readFile(filePath).toCharArray());
            takeBranch(branch, prefab);
        }
        catch (OpingParserException e)
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
            prefab.setPosition(forPosition(position));

        if (rotation != null)
            prefab.setRotation(forRotation(rotation));

        if (size != null)
            prefab.setSize(forSize(size));

        if (layer != null)
            prefab.setLayer(forLayer(layer));

        if (group != null)
            prefab.setGroup(forGroup(group));
    }

    private String forGroup(Leaf group)
    {
        if (group.size() != 1)
            throw new JMokaException("The group attribute is just one string value.");

        return getTestedValue(String.class, group.getString(0));
    }

    private Vector2 forSize(Leaf size)
    {
        if (size.size() != 2)
            throw new JMokaException("The size attribute should have 2 values.");

        float width = getTestedValue(Float.class, size.getString(0));
        float height = getTestedValue(Float.class, size.getString(1));

        return new Vector2(width, height);
    }

    private int forLayer(Leaf layer)
    {
        if (layer.size() != 1)
            throw new JMokaException("The layer attribute should have just one integer value.");

        return getTestedValue(Integer.class, layer.getString(0));
    }

    private float forRotation(Leaf rotation)
    {

        if (rotation.size() != 1)
            throw new JMokaException("The rotation attribute is just one float value.");

        Float value = getTestedValue(Float.class, rotation.getString(0));
        return (float) Math.toRadians(value);
    }

    private Vector2 forPosition(Leaf position)
    {
        if (position.size() != 2)
            throw new JMokaException("The position attribute should have 2 values.");

        float x = getTestedValue(Float.class, position.getString(0));
        float y = getTestedValue(Float.class, position.getString(1));

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
                namespace = NameManager.DEFAULT_NAMESPACE;

            Class<? extends Component> componentClass = Moka.getNameManager().findComponent(namespace, name);
            Prefab.ComponentAttrs attrs = new Prefab.ComponentAttrs();

            ArrayList<Method> methods = getQualifiedMethods(componentClass);

            for (Method method : methods)
            {
                ComponentAttribute attribute = method.getAnnotation(ComponentAttribute.class);
                Leaf leaf = componentBranch.takeLeaf(attribute.value());

                if (validateAttribute(attribute, leaf, method, componentClass.getSimpleName()))
                {
                    Class<?>[] params = getParamsFor(method);

                    Object[] castedValues = new Object[params.length];

                    for (int i = 0; i < params.length; i++)
                    {
                        String value = leaf.getString(i);
                        Object casted;

                        // we have to take different paths with different types, as we have
                        // to act differently when instantiating.
                        if (params[i].isAssignableFrom(Trigger.class))
                            casted = new TriggerPromise(Trigger.getStaticTriggerClass(value));
                        else if (params[i].isEnum())
                            casted = castEnumType(params[i], value);
                        else
                            casted = getTestedValue(params[i], value);

                        castedValues[i] = casted;
                    }

                    attrs.addMethodValues(method, castedValues);
                }
            }

            // check if all attributes where taken, if not, there's one or more that didn't
            // existed in the component.
            if (componentBranch.getLeafs().size() > 0)
            {
                StringBuilder builder = new StringBuilder();
                for (Leaf leaf : componentBranch.getLeafs())
                    builder.append(leaf.getName()).append(", ");

                throw new JMokaException("Attributes: " + builder + "does not exists in component "
                        + componentClass.getSimpleName());
            }

            prefab.getComponents().put(componentClass, attrs);
        }
    }
}
