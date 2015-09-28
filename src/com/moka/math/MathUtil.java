package com.moka.math;

public class MathUtil
{
    /**
     * Clamps the value between two bounds.
     *
     * @param value the value to be clamped.
     * @param min   the min bound.
     * @param max   the max bound.
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
    public static float mod(float a, float n)
    {
        return ((a % n) + n) % n;
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

    /**
     * Calculates the minimum rotation angle between two angles. (Thanks to fcasas).
     *
     * @param start the start angle in radians.
     * @param end the end angle in radians.
     * @return the minimum rotation angle.
     */
    public static float minAngle(float start, float end)
    {
        float round = (float) (Math.PI * 2);

        start = mod(start, round);
        end = mod(end, round);

        float delta = end - start;

        if (delta >= round / 2)
        {
            delta = delta - round;
        }

        if (delta <= - round / 2)
        {
            delta = delta + round;
        }

        return delta;
    }

    public static float lerp(int target, float value, float f)
    {
        return (target - value) * f;
    }
}
