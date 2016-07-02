package com.moka.tests;

import com.moka.physics.arcade.AxisAlignedBoundingBox;
import com.moka.physics.arcade.Circle;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ArcadePhysicsTests
{
    @Test
    public void testAABBVsAABBDetected()
    {
        AxisAlignedBoundingBox box1 = new AxisAlignedBoundingBox(0, 0, 100, 100);
        AxisAlignedBoundingBox box2 = new AxisAlignedBoundingBox(50, 50, 150, 150);

        assertThat("Boxes should collide",
                box1.collidesWith(box2), is(true));
    }

    @Test
    public void testAABBVsAABBNotDetected()
    {
        AxisAlignedBoundingBox box1 = new AxisAlignedBoundingBox(0, 0, 100, 100);
        AxisAlignedBoundingBox box2 = new AxisAlignedBoundingBox(150, 150, 250, 250);

        assertThat("Boxes shouldn't collide",
                box1.collidesWith(box2), is(false));
    }

    @Test
    public void testCircleVsCircleDetected()
    {
        Circle circle1 = new Circle(0, 0, 100);
        Circle circle2 = new Circle(50, 50, 100);

        assertThat("Circles should collide",
                circle1.collidesWith(circle2), is(true));
    }

    @Test
    public void testCircleVsCircleNotDetected()
    {
        Circle circle1 = new Circle(0, 0, 100);
        Circle circle2 = new Circle(150, 150, 100);

        assertThat("Circles should collide",
                circle1.collidesWith(circle2), is(false));
    }
}
