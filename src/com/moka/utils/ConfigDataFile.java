package com.moka.utils;

import com.shelodev.oping2.OpingParser;
import com.shelodev.oping2.structure.Branch;

public class ConfigDataFile
{
    private static OpingParser parser = new OpingParser();

    private FileHandle fileHandle;
    private Branch root;

    public ConfigDataFile(String filePath)
    {
        fileHandle = new FileHandle(filePath);

        if (fileHandle.exists())
            root = parser.parse(fileHandle.read().toCharArray());
    }

    public void setRoot(Branch branch)
    {
        this.root = branch;
    }

    public Branch getRoot()
    {
        return root;
    }

    public void save()
    {
        parser.render(root, fileHandle.getFile());
    }

    public boolean exists()
    {
        return fileHandle.exists();
    }

    @Override
    public String toString()
    {
        return root.toString(0);
    }
}
