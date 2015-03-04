package com.moka.graphics;

import com.moka.core.Utils;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

// TODO
public class Texture {
	private org.newdawn.slick.opengl.Texture texture;
	private String filePath;

	public Texture(String filePath) {
		this.filePath = filePath;

		String ext = Utils.getExtensionFrom(filePath);

		try {
			texture = TextureLoader.getTexture(ext, new FileInputStream(new File(filePath)), true, GL_NEAREST);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
	}

	public String getFilePath() {
		return filePath;
	}

	@Override
	public String toString() {
		return filePath;
	}

	public float getWidth() {
		return texture.getImageWidth();
	}

	public float getHeight() {
		return texture.getImageHeight();
	}

	public float getXTexCoord() {
		return texture.getWidth();
	}

	public float getYTexCoord() {
		return texture.getHeight();
	}
}
