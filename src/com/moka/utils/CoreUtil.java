package com.moka.utils;

import com.moka.graphics.Vertex;
import com.moka.math.Matrix3;
import com.moka.math.Matrix4;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.BufferUtils.createByteBuffer;

public class CoreUtil
{
    public static FloatBuffer genBuffer(Vertex[] vertices)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.SIZE);

        for (Vertex vertex : vertices)
        {
            buffer.put(vertex.getX());
            buffer.put(vertex.getY());
            buffer.put(vertex.getS());
            buffer.put(vertex.getT());
        }

        return (FloatBuffer) buffer.flip();
    }

    public static IntBuffer genBuffer(int[] indices)
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);

        for (int index : indices)
        {
            buffer.put(index);
        }

        return (IntBuffer) buffer.flip();
    }

    public static FloatBuffer genBuffer(Matrix4 matrix)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * 4);

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                buffer.put(matrix.get(j, i));
            }
        }

        return (FloatBuffer) buffer.flip();
    }

    public static FloatBuffer genBuffer(Matrix3 matrix)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(3 * 3);

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                buffer.put(matrix.get(j, i));
            }
        }

        return (FloatBuffer) buffer.flip();
    }

    public static String readFile(String filePath)
    {
        try
        {
            StringBuilder builder = new StringBuilder();
            FileReader reader = new FileReader(filePath);

            int c;
            while ((c = reader.read()) != -1)
                builder.append((char) c);

            reader.close();
            return builder.toString();
        }
        catch (FileNotFoundException e)
        {
            throw new JMokaException("File " + filePath + " not found.");
        }
        catch (IOException e)
        {
            throw new JMokaException("IOException when reading file " + filePath + ".");
        }
    }

    public static String getExtensionFrom(String filePath)
    {
        String[] s = filePath.split("\\.(?=[^\\.]+$)");
        return s[s.length - 1];
    }

    public static String getBaseDirectory(String filePath)
    {
        String[] s = filePath.split("/[^/]*$");
        return s[0];
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity)
    {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static void overrideFileWith(String filePath, String text)
    {
        FileHandle fileHandle = new FileHandle(filePath);
        fileHandle.truncate();
        fileHandle.append(text);
        fileHandle.save();
    }
}
