package com.moka.ui;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ComponentPanel extends JPanel
{
    private JSONObject component;

    public ComponentPanel(JSONObject component)
    {
        this.component = component;

        setLayout(new GridLayout(0, 2, 2, 2));

        List<String> attributes = getSortedAttributes();

        for (String attribute : attributes)
        {
            JLabel label = new JLabel(attribute);
            add(label);

            JTextField field = new JTextField();
            add(field);
        }

        setBorder(null);
    }

    public List<String> getSortedAttributes()
    {
        ArrayList<String> result = new ArrayList<>();

        Iterator iterator = component.keys();

        while (iterator.hasNext())
        {
            String key = (String) iterator.next();
            result.add(key);
        }

        Collections.sort(result);
        return result;
    }
}
