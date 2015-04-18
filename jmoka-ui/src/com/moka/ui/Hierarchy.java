package com.moka.ui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Hierarchy extends JTree
{
    private static final Hierarchy INSTANCE = newInstance();

    private Hierarchy(DefaultMutableTreeNode root)
    {
        super(root);
        setRootVisible(false);
        setShowsRootHandles(true);
        setDragEnabled(true);
        setDropMode(DropMode.INSERT);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
                }
            }
        });

    }

    private static Hierarchy newInstance()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

        DefaultMutableTreeNode cameraEntity = new DefaultMutableTreeNode("Camera");
        DefaultMutableTreeNode cameraComponent = new DefaultMutableTreeNode("Camera");

        DefaultMutableTreeNode playerEntity = new DefaultMutableTreeNode("Player");
        DefaultMutableTreeNode playerSprite = new DefaultMutableTreeNode("Sprite");
        DefaultMutableTreeNode playerShooter = new DefaultMutableTreeNode("Shooter");

        root.add(cameraEntity);
        root.add(playerEntity);

        final Hierarchy entities = new Hierarchy(root);

        cameraEntity.add(cameraComponent);

        playerEntity.add(playerSprite);
        playerEntity.add(playerShooter);

        return entities;
    }

    public static Hierarchy getInstance()
    {
        return INSTANCE;
    }

    public DefaultMutableTreeNode getCurrentEntity()
    {
        DefaultMutableTreeNode node = null;

        Object selected = getLastSelectedPathComponent();
        DefaultMutableTreeNode cur = (DefaultMutableTreeNode) selected;

        if (cur == null)
        {
            return null;
        }

        node = cur;
        if (!((DefaultMutableTreeNode) cur.getParent()).isRoot())
        {
            node = (DefaultMutableTreeNode) node.getParent();
        }

        return node;
    }

    public void addNewComponent(String name)
    {
        DefaultMutableTreeNode node = Hierarchy.getInstance().getCurrentEntity();

        if (node == null)
        {
            return;
        }

        DefaultMutableTreeNode component = new DefaultMutableTreeNode(name);
        node.add(component);

        expandPath(new TreePath(node.getPath()));

        Hierarchy.getInstance().updateUI();
    }

    public void addNewEntity(String name)
    {
        TreeModel model = getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        root.add(new DefaultMutableTreeNode(name));
        updateUI();
    }
}
