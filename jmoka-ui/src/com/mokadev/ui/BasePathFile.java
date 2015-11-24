package com.mokadev.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BasePathFile extends File
{
    public BasePathFile(String pathname)
    {
        super(pathname);
    }

    @Override
    public String toString()
    {
        return getName();
    }

    public char[] read()
    {
        if (!exists())
            return new char[0];

        StringBuilder builder = new StringBuilder();

        try
        {
            FileReader reader = new FileReader(this);
            {
                int c;
                while ((c = reader.read()) != -1)
                    builder.append((char) c);
            }
            reader.close();

            char[] value = new char[builder.length()];
            builder.getChars(0, builder.length(), value, 0);
            return value;
        }
        catch (FileNotFoundException e)
        {
            return new char[0];
        }
        catch (IOException e)
        {
            return new char[0];
        }
    }
}
