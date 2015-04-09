package com.moka.core.subengines;

import com.moka.core.SubEngine;
import com.moka.core.xml.XmlResourcesReader;
import com.moka.exceptions.JMokaException;
import com.moka.graphics.Texture;

import java.util.HashMap;

public class Resources extends SubEngine
{
	private XmlResourcesReader resourcesReader = new XmlResourcesReader(this);
	private HashMap<String, Object> resources = new HashMap<>();
	private HashMap<String, Texture> textures = new HashMap<>();

	public void loadResources(String filePath)
	{
		resourcesReader.read(filePath);
	}

	public Object get(String name)
	{
		if (!resources.containsKey(name))
		{
			throw new JMokaException("Resource with name" + name + " doesn't exists.");
		}

		return resources.get(name);
	}

	public String getString(String name)
	{
		return (String) resources.get(name);
	}

	public int getInt(String name)
	{
		return (int) get(name);
	}

	public int getIntOr(String name, int defaultValue)
	{
		if (has(name))
		{
			return (int) get(name);
		}
		else
		{
			return defaultValue;
		}
	}

	public float getFloat(String name)
	{
		return (float) get(name);
	}

	public double getDouble(String name)
	{
		return (double) get(name);
	}

	public boolean getBoolean(String name)
	{
		return (boolean) get(name);
	}

	public boolean has(String resource)
	{
		return resources.containsKey(resource);
	}

	public Object addResource(String name, Object value)
	{
		resources.put(name, value);
		return value;
	}

	public Texture addTexture(String path, Texture texture)
	{
		textures.put(path, texture);
		return texture;
	}

	public Texture getTextures(String path)
	{
		return textures.get(path);
	}

}
