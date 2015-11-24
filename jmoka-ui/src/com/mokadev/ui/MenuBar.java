package com.mokadev.ui;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar
{
    private final Main main;

    public MenuBar(Main main)
    {
        this.main = main;

        setup();
    }

    private void setup()
    {
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem("New file"));
        fileMenu.add(new JMenuItem("Save file"));
        fileMenu.add(new JMenuItem("Open directory"));

        JMenu directoryMenu = new JMenu("Directory");
        directoryMenu.add(new JMenuItem("New directory"));
        directoryMenu.add(new JMenuItem("Remove directory"));

        add(fileMenu);
        add(directoryMenu);
    }
}
