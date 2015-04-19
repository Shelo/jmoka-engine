package com.moka.ui.components;

import com.moka.ui.Hierarchy;
import com.moka.ui.JsonData;
import com.moka.utils.CoreUtil;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;

public class ComponentsList extends JList<String>
{
    private JFrame parent;

    private ComponentsList(ListModel<String> model, JFrame parent)
    {
        super(model);

        this.parent = parent;

        addMouseListener(new SelectComponent());

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public static JComponent newInstance(JFrame parent)
    {
        // create a list model to put in the list.
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // get the root json object to load component names.
        JSONObject object = JsonData.getInstance().getRoot();

        List<String> names = JsonData.getInstance().getComponentsNames();

        for (String name : names)
        {
            listModel.addElement(name);
        }

        // create the list with the list model.
        ComponentsList list = new ComponentsList(listModel, parent);

        // return the list with a scroll pane.
        return new JScrollPane(list);
    }

    private class SelectComponent extends MouseAdapter
    {
        @Override
        public void mousePressed(MouseEvent e)
        {
            if (e.getClickCount() == 2)
            {
                Hierarchy.getInstance().addNewComponent(getSelectedValue());
                parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
            }
        }
    }
}
