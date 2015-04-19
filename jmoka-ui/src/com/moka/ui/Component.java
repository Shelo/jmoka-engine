package com.moka.ui;

import java.util.HashMap;
import java.util.Map;

public class Component
{
    private Map<String, String> attributes = new HashMap<>();

    public void putAttr(String name, String value)
    {
        attributes.put(name, value);
    }
}
