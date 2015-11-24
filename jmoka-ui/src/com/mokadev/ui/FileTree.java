package com.mokadev.ui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.File;

public class FileTree extends JTree implements TreeSelectionListener
{
    private final Main main;

    public FileTree(Main main)
    {
        this.main = main;

        setup("jmoka-example/assets/prefabs");
    }

    private void setup(String directory)
    {
        setPreferredSize(new Dimension(200, 0));

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new BasePathFile(directory));
        createNodes(root);
        ((DefaultTreeModel) getModel()).setRoot(root);

        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        addTreeSelectionListener(this);
    }

    private void createNodes(DefaultMutableTreeNode parent)
    {
        BasePathFile[] files = filesFrom(((BasePathFile) parent.getUserObject()).getAbsolutePath());

        for (BasePathFile file : files)
        {
            if (file.isDirectory())
            {
                DefaultMutableTreeNode dir = new DefaultMutableTreeNode(file);
                createNodes(dir);
                parent.add(dir);
            }
            else
            {
                parent.add(new DefaultMutableTreeNode(file));
            }
        }
    }

    BasePathFile[] filesFrom(String directory)
    {
        File[] files = new File(directory).listFiles();

        if (files == null)
        {
            return new BasePathFile[0];
        }

        BasePathFile[] baseFiles = new BasePathFile[files.length];

        for (int i = 0; i < files.length; i++)
        {
            baseFiles[i] = new BasePathFile(files[i].getPath());
        }

        return baseFiles;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();

        if (node == null)
            return;

        if (node.isLeaf())
            main.getEditor().load((BasePathFile) node.getUserObject());
    }
}
