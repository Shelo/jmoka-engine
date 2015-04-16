package com.moka.math;

public class MathUtil
{
    /**
     * Clamps the value between two bounds.
     *
     * @param value the value to be clamped.
     * @param min the min bound.
     * @param max the max bound.
     * @return the bounded value.
     */
    public static float clamp(float value, float min, float max)
    {
        return value > max ? max : (value < min ? min : value);
    }

    /**
     * Calculates the mod in a "python" way. The operation is: a % n.
     *
     * @param a the first operand.
     * @param n the second operand.
     * @return the "python" mod result.
     */
    public static int mod(int a, int n)
    {
        return ((a % n) + n) % n;
    }
}
