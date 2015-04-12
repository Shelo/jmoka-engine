package com.moka.graphics;

import com.moka.core.Resources;
import com.moka.utils.CoreUtils;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Texture
{
    private org.newdawn.slick.opengl.Texture texture;
    private String filePath;

    public Texture(String filePath)
    {
        this.filePath = filePath;

        if (filePath != null)
        {
            String ext = CoreUtils.getExtensionFrom(filePath);

            try
            {
                texture = TextureLoader.getTexture(ext, new FileInputStream(new File(filePath)), false, GL_NEAREST);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
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
        return texture.getImageWidth();
    }

    public float getHeight()
    {
        return texture.getImageHeight();
    }

    public float getTexCoordX()
    {
        return texture.getWidth();
    }

    public float getTexCoordY()
    {
        return texture.getHeight();
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
