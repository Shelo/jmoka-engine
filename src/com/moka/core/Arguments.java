package com.moka.core;

import java.util.HashMap;

/**
 * Created as a way of passing various argument between components from the factory.
 * @author Shelo
 */
public class Arguments extends HashMap<String, Object> {
	public Arguments put(String key, Object value) {
		super.put(key, value);
		return this; 
	}
}
