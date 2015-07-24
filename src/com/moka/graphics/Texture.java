package com.moka.graphics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import static org.lwjgl.opengl.GL11.*;

public class Texture
{
    public enum Filter
    {
        NEAREST,
        LINEAR
    }

    private int textureId;
    private int height;
    private int width;

    public Texture(String filePath, Filter filter)
    {
        if (filePath != null)
        {
            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer components = BufferUtils.createIntBuffer(1);

            ByteBuffer imageBuffer = STBImage.stbi_load(filePath, width, height, components, 4);

            textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, getFilter(filter));
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, getFilter(filter));

            this.width = width.get();
            this.height = height.get();

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA,
                    GL_UNSIGNED_BYTE, imageBuffer);

            STBImage.stbi_image_free(imageBuffer);
        }
    }

    public Texture(String filePath)
    {
        this(filePath, Filter.NEAREST);
    }

    private int getFilter(Filter filter)
    {
        if (filter == Filter.NEAREST)
        {
            return GL_NEAREST;
        }
        else
        {
            return GL_LINEAR;
        }
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public float getTexCoordX()
    {
        return 1;
    }

    public float getTexCoordY()
    {
        return 1;
    }
}
