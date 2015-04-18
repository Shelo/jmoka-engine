package com.moka.ui;

import com.moka.ui.top.NewComponent;
import com.moka.ui.top.NewEntity;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JToolBar
{
    private static ToolBar INSTANCE;

    private Hierarchy hierarchy;

    private ToolBar(Hierarchy hierarchy)
    {
        this.hierarchy = hierarchy;

        setFloatable(false);
        setRollover(true);

        JButton addNewEntityButton = new JButton();
        addNewEntityButton.setText("New Entity");
        addNewEntityButton.addActionListener(new NewEntityWindow());
        add(addNewEntityButton);

        JButton addNewComponentButton = new JButton();
        addNewComponentButton.setText("Add Component");
        addNewComponentButton.addActionListener(new NewComponentWindow());
        add(addNewComponentButton);

        add(Box.createHorizontalGlue());

        JButton newButton = new JButton();
        newButton.setText("New Scene");
        add(newButton);

        JButton loadButton = new JButton();
        loadButton.setText("Load");
        add(loadButton);

        JButton saveButton = new JButton();
        saveButton.setText("Save");
        add(saveButton);
    }

    private class NewComponentWindow implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            // open the component frame.
            new NewComponent(hierarchy);
        }
    }

    public static ToolBar getInstance()
    {
        if (INSTANCE == null)
        {
            throw new NullPointerException("ToolBar singleton not initialized.");
        }

        return INSTANCE;
    }

    public static ToolBar getInstance(Hierarchy hierarchy)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ToolBar(hierarchy);
        }

        return INSTANCE;
    }

    private class NewEntityWindow implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent actionEvent)
        {
            new NewEntity();
        }
    }
}
