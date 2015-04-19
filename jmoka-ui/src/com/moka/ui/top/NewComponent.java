package com.moka.ui.top;

import com.moka.ui.Hierarchy;
import com.moka.ui.components.ComponentsList;

import javax.swing.*;
import java.awt.*;

public class NewComponent extends JFrame
{
    public NewComponent(Hierarchy hierarchy)
    {
        setLayout(new BorderLayout());
        setTitle("Add component");
        setSize(200, 400);
        setLocationRelativeTo(null);

        JComponent list = ComponentsList.newInstance(this);
        add(list, BorderLayout.CENTER);

        setVisible(true);
    }
}
