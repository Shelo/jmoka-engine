package com.moka.utils;

import com.shelodev.oping2.OpingParser;
import com.shelodev.oping2.structure.Branch;
import com.shelodev.oping2.structure.Leaf;

/**
 * Config that uses Oping markup files to load the data into this object, and also save data.
 * This can be used as a Branch, just as any Oping Branch object.
 *
 * @author mijara
 */
public class ConfigDataFile extends Branch
{
    private static OpingParser parser = new OpingParser();

    private FileHandle fileHandle;
    // private Branch root;

    public ConfigDataFile(String filePath)
    {
        fileHandle = new FileHandle(filePath);

        Branch root;
        if (fileHandle.exists()) {
            root = parser.parse(fileHandle.read().toCharArray());
        } else {
            root = new Branch();
        }

        for (Leaf leaf : root.getLeafs())
            addLeaf(leaf);

        for (Branch branch : root.getBranches())
            addBranch(branch);

        if (root.getName() != null)
            setName(root.getName());

        if (root.getNamespace() != null)
            setNamespace(root.getNamespace());
    }

    public void save()
    {
        parser.render(this, fileHandle.getFile());
    }

    public boolean exists()
    {
        return fileHandle.exists();
    }

    @Override
    public String toString()
    {
        return toString(0);
    }

    public FileHandle getFileHandle()
    {
        return fileHandle;
    }
}
