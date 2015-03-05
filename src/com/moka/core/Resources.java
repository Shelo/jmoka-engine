package com.moka.core;

import com.moka.core.xml.XmlResourcesReader;
import com.moka.exceptions.JMokaException;
import com.moka.graphics.Texture;

import java.util.HashMap;

public class Resources {
	private static XmlResourcesReader resourcesReader = new XmlResourcesReader();
	private static HashMap<String, Object> resources = new HashMap<>();
	private static HashMap<String, Texture> textures = new HashMap<>();

	public static String getString(String name) {
		return (String) resources.get(name);
	}

	public static int getInt(String name) {
		return (int) get(name);
	}

	public static float getFloat(String name) {
		return (float) get(name);
	}

	public static double getDouble(String name) {
		return (double) get(name);
	}

	public static boolean getBoolean(String name) {
		return (boolean) get(name);
	}

	public static Object get(String name) {
		if (!resources.containsKey(name))
			throw new JMokaException("Resource with name" + name + " doesn't exists.");

		return resources.get(name);
	}

	public static Texture getTextures(String path) {
		return textures.get(path);
	}

	public static Object addResource(String name, Object value) {
		resources.put(name, value);
		return value;
	}

	public static Texture addTexture(String path, Texture texture) {
		textures.put(path, texture);
		return texture;
	}

	public static void loadResources(String filePath) {
		resourcesReader.read(filePath);
	}
}
