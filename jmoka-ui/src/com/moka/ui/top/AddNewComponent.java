package com.moka.ui.top;

import com.moka.ui.Hierarchy;
import com.moka.ui.components.AllComponentsList;

import javax.swing.*;
import java.awt.*;

public class AddNewComponent extends JFrame
{
    public AddNewComponent(Hierarchy hierarchy)
    {
        setLayout(new BorderLayout());
        setTitle("Add new component");
        setSize(200, 400);
        setLocationRelativeTo(null);

        JComponent list = AllComponentsList.newInstance(hierarchy, this);
        add(list, BorderLayout.CENTER);

        setVisible(true);
    }
}
