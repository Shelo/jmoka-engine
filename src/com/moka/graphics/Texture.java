package com.moka.graphics;

import com.moka.core.Resources;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import static org.lwjgl.opengl.GL11.*;

public class Texture
{
    private String filePath;
    private int textureId;
    private int height;
    private int width;

    public Texture(String filePath)
    {
        this.filePath = filePath;

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

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            this.width = width.get();
            this.height = height.get();

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA,
                    GL_UNSIGNED_BYTE, imageBuffer);

            STBImage.stbi_image_free(imageBuffer);
        }
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public String getFilePath()
    {
        return filePath;
    }

    @Override
    public String toString()
    {
        return filePath;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
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

    /**
     * Loads a new texture and stores it at the resources. If the texture was previously loaded,
     * then this returns that texture instead of creating a new one.
     *
     * @param resources resources where the texture should look.
     * @param path      path of the texture in the file system.
     * @return the requested texture.
     */
    public static Texture newTexture(Resources resources, String path)
    {
        if (!resources.hasTexture(path))
        {
            return resources.addTexture(path, new Texture(path));
        }
        else
        {
            return resources.getTexture(path);
        }
    }
}
