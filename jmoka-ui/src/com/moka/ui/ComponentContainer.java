package com.moka.ui;

import javax.swing.*;
import java.awt.*;

public class ComponentContainer extends JPanel
{
    private static final ComponentContainer INSTANCE = new ComponentContainer();

    private ComponentPanel componentPanel;

    private ComponentContainer()
    {
        setLayout(new BorderLayout());
    }

    public void setComponentPanel(String name)
    {
        removeAll();

        componentPanel = new ComponentPanel(JsonData.getInstance().getComponent(name));
        add(componentPanel, BorderLayout.PAGE_START);
        updateUI();
    }

    public ComponentPanel getComponentPanel()
    {
        return componentPanel;
    }

    public static ComponentContainer getInstance()
    {
        return INSTANCE;
    }
}
