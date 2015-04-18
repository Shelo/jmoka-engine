package com.moka.ui;

import com.moka.ui.components.AllComponentsList;
import com.moka.ui.top.AddNewComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JToolBar
{
    private Hierarchy hierarchy;

    public ToolBar(Hierarchy hierarchy)
    {
        this.hierarchy = hierarchy;

        setFloatable(false);
        setRollover(true);

        JButton addNewEntityButton = new JButton();
        addNewEntityButton.setText("New Entity");
        add(addNewEntityButton);

        JButton addNewComponentButton = new JButton();
        addNewComponentButton.setText("New Component");
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
            new AddNewComponent(hierarchy);
        }
    }
}
