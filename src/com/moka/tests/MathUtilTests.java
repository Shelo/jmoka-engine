package com.moka.tests;

import com.moka.math.MathUtil;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MathUtilTests
{
    @Test
    public void clamp()
    {
        float value = MathUtil.clamp(30, -30, 20);

        assertThat(value, is(20.0f));
    }

    @Test
    public void modInt()
    {
        assertThat(MathUtil.mod(-4, 2), is(0));
    }

    @Test
    public void modFloat()
    {
        assertThat(MathUtil.mod(-4.5f, 2.0f), is(1.5f));
    }

    @Test
    public void minAnglePositive()
    {
        float rotation = MathUtil.minAngle((float) Math.toRadians(45), (float) Math.toRadians(180));

        assertEquals(rotation, Math.toRadians(135.0f), 0.000001f);
    }

    @Test
    public void minAngleNegative()
    {
        float rotation = MathUtil.minAngle((float) Math.toRadians(45), (float) Math.toRadians(270));

        assertEquals(rotation, Math.toRadians(-135.0f), 0.000001f);
    }

    @Test
    public void moveTowardsPositive()
    {
        float next = MathUtil.moveTowards(10, 100, 10);

        assertThat(next, is(20.0f));
    }

    @Test
    public void moveTowardsNegative()
    {
        float next = MathUtil.moveTowards(10, -100, 10);

        assertThat(next, is(0.0f));
    }

    @Test
    public void moveTowardsPositiveArrive()
    {
        float next = MathUtil.moveTowards(10, 20, 15);

        assertThat(next, is(20.0f));
    }

    @Test
    public void moveTowardsNegativeArrive()
    {
        float next = MathUtil.moveTowards(0, -10, 15);

        assertThat(next, is(-10.f));
    }
}
