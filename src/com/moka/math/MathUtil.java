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
            delta = delta - round;

        if (delta <= - round / 2)
            delta = delta + round;

        return delta;
    }

    /**
     * Linear interpolation between two values and a given factor.
     *
     * @param target    the final value.
     * @param value     the initial value.
     * @param f         the factor.
     * @return          the interpolated value.
     */
    public static float lerp(int target, float value, float f)
    {
        return (target - value) * f;
    }

    /**
     * Moves a value with a step distance every call, until in reaches the target.
     *
     * The last value (when the origin is too near the target) is always the target value.
     *
     * @param origin the origin of the value.
     * @param target the final value.
     * @param step absolute distance to travel in one call.
     * @return the next value.
     */
    public static float moveTowards(float origin, float target, float step)
    {
        float distance =  target - origin;
        int sign = (int) (Math.abs(distance) / distance);

        if (Math.abs(distance) < step) {
            return target;
        }

        return origin + sign * step;
    }
}
