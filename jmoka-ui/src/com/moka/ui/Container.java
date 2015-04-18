package com.moka.ui;

import javax.swing.*;

public class Container extends JSplitPane
{
    private Hierarchy hierarchy;

    public Container()
    {
        super(JSplitPane.HORIZONTAL_SPLIT);

        hierarchy = Hierarchy.newInstance();
        JScrollPane scrollPane = new JScrollPane(hierarchy);
        setLeftComponent(scrollPane);

        Component component = new Component();
        setRightComponent(component);

        setOneTouchExpandable(true);
        setDividerLocation(300);
    }

    public Hierarchy getHierarchy()
    {
        return hierarchy;
    }
}
