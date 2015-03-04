package com.moka.core;

import com.moka.core.xml.XmlResourcesReader;

import java.util.HashMap;

public class Resources {
	private static XmlResourcesReader resourcesReader;
	private static HashMap<String, Object> resources;

	static {
		resources = new HashMap<>();
		resourcesReader = new XmlResourcesReader();
	}

	public static String getString(String name) {
		return null;
	}

	public static int getInt(String name) {
		return 0;
	}

	public static float getFloat(String name) {
		return 0;
	}

	public static double getDouble(String name) {
		return 0;
	}

	public static boolean getBoolean(String name) {
		return false;
	}
	
	public static void addResource(String name, Object value) {

	}
}
