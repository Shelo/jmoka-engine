package com.moka.ui;

import com.moka.utils.CoreUtil;
import com.moka.utils.JMokaException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class JsonData
{
    private static final JsonData INSTANCE = new JsonData();
    private static final String COMPONENTS_JSON_FILE = "gen/components.json";

    private JSONObject root;

    public JsonData()
    {
        String content = CoreUtil.readFile(COMPONENTS_JSON_FILE);

        try
        {
            root = new JSONObject(content);
        }
        catch (JSONException e)
        {
            throw new JMokaException("");
        }
    }

    public JSONObject getComponent(String name)
    {
        JSONObject component = null;

        try
        {
            component = root.getJSONObject(name);
        }
        catch (JSONException e)
        {
            throw new JMokaException("There was an JSON Exception:" + e.getMessage());
        }

        return component;
    }

    public List<String> getComponentsNames()
    {
        List<String> names = new LinkedList<>();

        // get the iterator and add all component names to the list model.
        Iterator iterator = root.keys();
        while (iterator.hasNext())
        {
            String key = (String) iterator.next();
            names.add(key);
        }

        return names;
    }

    public JSONObject getRoot()
    {
        return root;
    }

    public static JsonData getInstance()
    {
        return INSTANCE;
    }
}
