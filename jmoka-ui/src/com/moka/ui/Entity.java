package com.moka.ui;

import java.util.ArrayList;
import java.util.List;

public class Entity
{
    List<Component> components = new ArrayList<>();

    public void add(Component component)
    {
        components.add(component);
    }
}
