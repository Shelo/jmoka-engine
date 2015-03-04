package com.moka.physics;

import com.moka.core.JMokaLog;
import com.moka.exceptions.JMokaException;
import com.moka.math.Vector2;

public class Projection {
    public final float min;
    public final float max;

    public Projection(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public Projection(Vector2[] vertices, Vector2 axis) {
        if (vertices.length == 0)
            throw new JMokaException("Empty vertices");

        float min = axis.dot(vertices[0]);
        float max = min;

        for (int i = 1; i < vertices.length; i++) {
            float cur = axis.dot(vertices[i]);

            if (cur < min)
                min = cur;

            else if (cur > max)
                max = cur;
        }

        this.min = min;
        this.max = max;
    }

    public boolean overlaps(Projection other) {
        return !(min >= other.max || other.min >= max);
    }

    public float getOverlap(Projection other) {
        return (max < other.max) ? other.min - max : other.max - min;
    }
}
