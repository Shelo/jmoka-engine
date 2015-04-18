package com.moka.ui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Hierarchy extends JTree
{
    public Hierarchy(DefaultMutableTreeNode root)
    {
        super(root);
        setRootVisible(false);
        setShowsRootHandles(true);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
                    System.out.println(selectedNode.getUserObject().toString());
                }
            }
        });
    }

    public static Hierarchy newInstance()
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
}
