package com.moka.ui.components;

import com.moka.ui.Hierarchy;
import com.moka.utils.CoreUtil;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Iterator;

public class AllComponentsList extends JList<String>
{
    private Hierarchy hierarchy;
    private JFrame parent;

    private AllComponentsList(ListModel<String> model, JFrame parent, Hierarchy hierarchy)
    {
        super(model);

        this.hierarchy = hierarchy;
        this.parent = parent;

        addMouseListener(new SelectComponent());

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public static JComponent newInstance(Hierarchy hierarchy, JFrame parent)
    {
        // create a list model to put in the list.
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // get the root json object to load component names.
        JSONObject object = loadJson();

        // get the iterator and add all component names to the list model.
        Iterator iterator = object.keys();
        while (iterator.hasNext())
        {
            String key = (String) iterator.next();
            listModel.addElement(key);
        }

        // create the list with the list model.
        AllComponentsList list = new AllComponentsList(listModel, parent, hierarchy);

        // return the list with a scroll pane.
        return new JScrollPane(list);
    }

    public static JSONObject loadJson()
    {
        JSONObject object = null;

        try
        {
            object = new JSONObject(CoreUtil.readFile("gen/components.json"));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return object;
    }

    private class SelectComponent extends MouseAdapter
    {
        @Override
        public void mousePressed(MouseEvent e)
        {
            if (e.getClickCount() == 2)
            {
                String value = getSelectedValue();
                System.out.println(value);

                parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
            }
        }
    }
}
