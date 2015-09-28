package com.moka.utils;

import java.io.*;
import java.nio.channels.FileChannel;

public class FileHandle
{
    private static StringBuilder builder = new StringBuilder();

    private StringBuilder append = new StringBuilder();
    private File file;

    public FileHandle(String path)
    {
        file = new File(path);
    }

    public String read()
    {
        if (!file.exists())
            throw new JMokaException("File does not exists.");

        try
        {
            FileReader reader = new FileReader(file);

            int c;
            while ((c = reader.read()) != -1)
                builder.append((char) c);

            reader.close();
            return builder.toString();
        }
        catch (FileNotFoundException e)
        {
            throw new JMokaException("File " + file.getPath() + " not found.");
        }
        catch (IOException e)
        {
            throw new JMokaException("IOException when reading file " + file.getPath() + ".");
        }
    }

    /**
     * Append text to the file, but doesn't save the file.
     * @param text the text to append.
     */
    public void append(String text)
    {
        append.append(text);
    }

    public void truncate()
    {
        try
        {
            FileChannel outChan = new FileOutputStream(file, true).getChannel();
            outChan.truncate(0);
            outChan.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void save()
    {
        try
        {
            if (!file.exists())
                file.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            String content = read();

            FileWriter writer = new FileWriter(file);
            {
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                {
                    bufferedWriter.write(content);
                    bufferedWriter.write(append.toString());
                }
                bufferedWriter.close();
            }
            writer.close();

            append.delete(0, append.length() - 1);
        }
        catch (IOException e)
        {
            throw new JMokaException(e.getMessage());
        }
    }
}
