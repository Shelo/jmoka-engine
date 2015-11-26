package com.moka.utils;

import com.moka.math.Vector2;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Wrapper for pool static classes.
 *
 * Pool classes have a general contract: take, put and never use again, i.e. after taking an object
 * from a given pool, you should use it and then put it on the pool knowing well that this object
 * should never be used again with another reference other than one given by take methods.
 */
public class Pools
{
    /**
     * Pool for Vector2 objects. This uses a Blocking Queue for storing.
     */
    public static class vec2
    {
        private static Queue<Vector2> objects = new LinkedBlockingQueue<>();

        /**
         * Takes a vector2 object from the pool setting x and y values.
         *
         * @param x     x position for the vector.
         * @param y     y position for the vector.
         * @return      a {@link Vector2} object always distinct to null.
         */
        public static Vector2 take(float x, float y)
        {
            Vector2 object = objects.poll();

            if (object == null)
                object = new Vector2(x, y);

            return object.set(x, y);
        }

        /**
         * Takes a vector2 object from the pool setting x and y values from a vector2 copy.
         *
         * @param value     the vector2 copy.
         * @return          a vector2 object with x and y values from the copy.
         */
        public static Vector2 take(Vector2 value)
        {
            return take(value.x, value.y);
        }

        /**
         * Takes a vector2 object from the pool setting x and y values to 0.
         *
         * @return          a zero vector2 object.
         */
        public static Vector2 take()
        {
            return take(0, 0);
        }

        /**
         * Puts a {@link Vector2} object inside the pool for future use.
         * See the contract in {@link Pools} for objects.
         *
         * @param value     the object to be put.
         */
        public static void put(Vector2 value)
        {
            objects.add(value);
        }
    }
}
