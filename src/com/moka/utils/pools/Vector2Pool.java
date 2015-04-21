package com.moka.utils.pools;

import com.moka.math.Vector2;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Vector2Pool
{
    private static Queue<Vector2> objects = new LinkedBlockingQueue<>();

    public static void put(Vector2 value)
    {
        objects.add(value);
        System.out.println(objects.size());
    }

    public static Vector2 take()
    {
        Vector2 object = objects.poll();

        if (object == null)
        {
            object = new Vector2();
        }

        return object;
    }
}
