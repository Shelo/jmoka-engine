package com.mokadev.ui;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame
{
    private Editor editor;

    public Main()
    {
        setup();
    }

    void setup()
    {
        setLayout(new BorderLayout());

        editor = new Editor();
        add(editor, BorderLayout.CENTER);

        Files files = new Files(this);
        add(files, BorderLayout.WEST);

        pack();

        setTitle("jMoka Oping Editor");
        setMinimumSize(new Dimension(200, 200));
        setSize(new Dimension(800, 600));
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
