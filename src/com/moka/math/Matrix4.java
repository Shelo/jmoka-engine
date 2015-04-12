package com.moka.math;

public class Matrix4
{
    private float[][] m;

    public Matrix4()
    {
        m = new float[4][4];
    }

    public Matrix4 toTranslation(float x, float y, float z)
    {
        m[0][0] = 1;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = x;

        m[1][0] = 0;
        m[1][1] = 1;
        m[1][2] = 0;
        m[1][3] = y;

        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = 1;
        m[2][3] = z;

        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Matrix4 toScale(float x, float y, float z)
    {
        m[0][0] = x;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = 0;
        m[1][0] = 0;
        m[1][1] = y;
        m[1][2] = 0;
        m[1][3] = 0;
        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = z;
        m[2][3] = 0;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    private Matrix4 initOrthographic(float left, float right, float bottom, float top, float near, float far)
    {
        float width = right - left;
        float height = top - bottom;
        float depth = far - near;

        m[0][0] = 2 / width;
        m[0][1] = 0;
        m[0][2] = 0;
        m[0][3] = -(right + left) / width;

        m[1][0] = 0;
        m[1][1] = 2 / height;
        m[1][2] = 0;
        m[1][3] = -(top + bottom) / height;

        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = -2 / depth;
        m[2][3] = -(far + near) / depth;

        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;

        return this;
    }

    public Matrix4 initRotation(Vector3f forward, Vector3f up, Vector3f right)
    {
        m[0][0] = right.x;
        m[0][1] = right.y;
        m[0][2] = right.z;
        m[0][3] = 0;
        m[1][0] = up.x;
        m[1][1] = up.y;
        m[1][2] = up.z;
        m[1][3] = 0;
        m[2][0] = forward.x;
        m[2][1] = forward.y;
        m[2][2] = forward.z;
        m[2][3] = 0;
        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;
        m[3][3] = 1;
        return this;
    }

    public Vector3f transform(Vector3f r)
    {
        return new Vector3f(
                m[0][0] * r.x + m[0][1] * r.y + m[0][2] * r.z + m[0][3],
                m[1][0] * r.x + m[1][1] * r.y + m[1][2] * r.z + m[1][3],
                m[2][0] * r.x + m[2][1] * r.y + m[2][2] * r.z + m[2][3]
        );
    }

    public Matrix4 mul(Matrix4 r)
    {
        Matrix4 res = new Matrix4();

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                res.set(i, j,
                        m[i][0] * r.get(0, j) + m[i][1] * r.get(1, j) + m[i][2] * r.get(2, j)
                                + m[i][3] * r.get(3, j));
            }
        }

        return res;
    }

    public Matrix4 mul(Matrix4 r, Matrix4 res)
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                res.set(i, j, m[i][0] * r.get(0, j) + m[i][1] * r.get(1, j) + m[i][2] * r.get(2, j)
                        + m[i][3] * r.get(3, j));
            }
        }

        return res;
    }

    public Vector2f mul(Vector2f vector)
    {
        return new Vector2f(m[0][0] * vector.x + m[0][1] * vector.y + m[0][3],
                m[1][0] * vector.x + m[1][1] * vector.y + m[1][3]);
    }

    public float[][] getM()
    {
        float[][] res = new float[4][4];

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                res[i][j] = m[i][j];
            }
        }

        return res;
    }

    public float get(int x, int y)
    {
        return m[x][y];
    }

    public void set(int x, int y, float value)
    {
        m[x][y] = value;
    }

    public static Matrix4 orthographic(float left, float right, float bottom, float top, float zNear, float zFar)
    {
        return new Matrix4().initOrthographic(left, right, bottom, top, zNear, zFar);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for (int x = 0; x < 4; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                builder.append(get(x, y)).append(" ");
            }

            builder.append("\n");
        }

        return builder.toString();
    }
}