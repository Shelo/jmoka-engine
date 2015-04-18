package com.moka.core.sync;

import org.json.JSONObject;

public class ComponentDescriptor
{
    private StringBuilder stringBuilder;
    private JSONObject jsonObject;

    public ComponentDescriptor(StringBuilder stringBuilder, JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
        this.stringBuilder = stringBuilder;
    }

    public JSONObject getJsonObject()
    {
        return jsonObject;
    }

    public StringBuilder getStringBuilder()
    {
        return stringBuilder;
    }
}
