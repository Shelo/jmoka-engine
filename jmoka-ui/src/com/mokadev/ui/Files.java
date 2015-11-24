package com.mokadev.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class Files extends JList<BasePathFile>
{
    private final Main main;

    public Files(Main main)
    {
        this.main = main;

        setup();
    }

    void setup()
    {
        setPreferredSize(new Dimension(150, 0));

        // load files.
        loadFiles("jmoka-example/assets/prefabs");

        // open the first file in the editor at startup.
        main.getEditor().load(getModel().getElementAt(0));

        // mouse listener to catch double-click on a file item.
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2) {
                    int index = locationToIndex(e.getPoint());
                    BasePathFile element = getModel().getElementAt(index);
                    main.getEditor().load(element);
                }
            }
        });
    }

    void loadFiles(String directory)
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
