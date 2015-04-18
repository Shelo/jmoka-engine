package com.moka.ui.top;

import com.moka.ui.Hierarchy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class NewEntity extends JFrame
{
    public NewEntity()
    {
        setTitle("Add new entity");
        setSize(300, 100);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Entity unique name:");
        add(label, BorderLayout.PAGE_START);

        final JTextField name = new JTextField();
        name.setAction(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                Hierarchy.getInstance().addNewEntity(name.getText());
                dispatchEvent(new WindowEvent(NewEntity.this, WindowEvent.WINDOW_CLOSING));
            }
        });

        add(name, BorderLayout.PAGE_END);

        setVisible(true);
    }


}
