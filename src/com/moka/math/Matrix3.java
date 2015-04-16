package com.moka.math;

import com.moka.utils.JMokaException;

public class Matrix3
{
    private float[][] values = new float[3][3];

    public Matrix3 toTranslation(float x, float y)
    {
        values[0][0] = 1;
        values[0][1] = 0;
        values[0][2] = x;

        values[1][0] = 0;
        values[1][1] = 1;
        values[1][2] = y;

        values[2][0] = 0;
        values[2][1] = 0;
        values[2][2] = 1;

        return this;
    }

    public Matrix3 toScale(float x, float y)
    {
        values[0][0] = x;
        values[0][1] = 0;
        values[0][2] = 0;

        values[1][0] = 0;
        values[1][1] = y;
        values[1][2] = 0;

        values[2][0] = 0;
        values[2][1] = 0;
        values[2][2] = 1;

        return this;
    }

    public Matrix3 toRotation(float radians)
    {
        values[0][0] = (float) Math.cos(radians);
        values[1][0] = (float) Math.sin(radians);
        values[2][0] = 0;

        values[0][1] = - values[1][0];
        values[1][1] = values[0][0];
        values[2][1] = 0;

        values[0][2] = 0;
        values[1][2] = 0;
        values[2][2] = 1;

        return this;
    }

    public Matrix3 toOrthographic(float left, float right, float bottom, float top)
    {
        float width = right - left;
        float height = top - bottom;

        values[0][0] = 2 / width;
        values[0][1] = 0;
        values[0][2] = - (right + left) / width;

        values[1][0] = 0;
        values[1][1] = 2 / height;
        values[1][2] = - (top + bottom) / height;

        values[2][0] = 0;
        values[2][1] = 0;
        values[2][2] = 1;

        return this;
    }

    public void set(int row, int col, float value)
    {
        values[row][col] = value;
    }

    public float get(int row, int col)
    {
        return values[row][col];
    }

    public Matrix3 mul(Matrix3 r, Matrix3 res)
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                res.set(i, j, values[i][0] * r.get(0, j) + values[i][1] * r.get(1, j) + values[i][2] * r.get(2, j));
            }
        }

        return res;
    }

    public Vector2f mul(final Vector2f r, Vector2f result)
    {
        if (result == null)
        {
            throw new JMokaException("The result vector given is null.");
        }

        result.x = values[0][0] * r.x + values[0][1] * r.y + values[0][2];
        result.y = values[1][0] * r.x + values[1][1] * r.y + values[1][2];
        return result;
    }

    public void set(Matrix3 other)
    {
        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                set(x, y, other.get(x, y));
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                builder.append(get(x, y)).append(" ");
            }

            builder.append("\n");
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Matrix3)
        {
            Matrix3 other = (Matrix3) obj;

            for (int x = 0; x < 3; x++)
            {
                for (int y = 0; y < 3; y++)
                {
                    if (values[x][y] != other.get(x, y))
                    {
                        return false;
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
