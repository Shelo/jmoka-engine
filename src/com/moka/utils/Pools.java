package com.moka.utils;

import com.moka.math.Vector2;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pools
{
    public static class vec2
    {
        private static Queue<Vector2> objects = new LinkedBlockingQueue<>();

        public static void put(Vector2 value)
        {
            objects.add(value);
        }

        public static Vector2 take(float x, float y)
        {
            Vector2 object = objects.poll();

            if (object == null)
            {
                object = new Vector2(x, y);
            }

            return object.set(x, y);
        }

        public static Vector2 take()
        {
            return take(0, 0);
        }
    }
}
