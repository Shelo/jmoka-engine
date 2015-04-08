package com.moka.math;

public class MokaMath
{
    public static float clamp(float value, float min, float max)
    {
        return value > max ? max : (value < min ? min : value);
    }
}
