package com.moka.ui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class Main extends JFrame
{
    public Main()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(600, 600);
        setLocationRelativeTo(null);
        setTitle("Hierarchy");

        Container container = new Container();
        add(container, BorderLayout.CENTER);

        ToolBar toolBar = new ToolBar(container.getHierarchy());
        add(toolBar, BorderLayout.PAGE_START);

        setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new Main();
            }
        });
    }
}
