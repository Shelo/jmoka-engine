package com.moka.resources.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public abstract class AbstractBuffer<T> implements Iterable<T>
{
    private ArrayList<T> objects;

    public AbstractBuffer(int initialCapacity)
    {
        objects = new ArrayList<>(initialCapacity);
    }

    public void add(T object)
    {
        objects.add(object);
    }

    public void add(T... objects)
    {
        Collections.addAll(this.objects, objects);
    }

    public T get(int i)
    {
        return objects.get(i);
    }

    public T get()
    {
        return get(0);
    }

    @Override
    public Iterator<T> iterator()
    {
        return objects.iterator();
    }

    public int size()
    {
        return objects.size();
    }
}
