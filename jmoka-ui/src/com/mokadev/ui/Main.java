package com.mokadev.ui;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame
{
    private Editor editor = new Editor();

    public Main()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setBackground(Color.WHITE);
        setContentPane(panel);

        setLayout(new BorderLayout());
        setComponents();
        pack();
        setup();
    }

    private void setComponents()
    {
        add(editor, BorderLayout.CENTER);
        add(new FileTree(this), BorderLayout.WEST);
        setJMenuBar(new MenuBar(this));
    }

    void setup()
    {
        setTitle("jMoka Oping Editor");
        setMinimumSize(new Dimension(200, 200));
        setSize(new Dimension(900, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public Editor getEditor()
    {
        return editor;
    }

    public static void main(String[] args)
    {
        EventQueue.invokeLater(Main::new);
    }
}
