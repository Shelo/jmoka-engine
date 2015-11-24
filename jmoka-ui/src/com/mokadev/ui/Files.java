package com.mokadev.ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;

public class Files extends JList
{
    private final Main main;
    private String directory;

    public Files(Main main)
    {
        this.main = main;

        setup();
    }

    void setup()
    {
        directory = "jmoka-example/assets/prefabs";

        setPreferredSize(new Dimension(150, 0));
        loadFiles();

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2) {
                    int index = locationToIndex(e.getPoint());
                    BasePathFile element = (BasePathFile) getModel().getElementAt(index);
                    main.getEditor().load(element);
                }
            }
        });
    }

    void loadFiles()
    {
        File[] files = new File(directory).listFiles();

        if (files == null)
            return;

        BasePathFile[] baseFiles = new BasePathFile[files.length];

        for (int i = 0; i < files.length; i++)
            baseFiles[i] = new BasePathFile(files[i].getPath());

        setListData(baseFiles);
    }
}
