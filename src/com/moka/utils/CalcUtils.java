package com.moka.utils;

import com.moka.core.Transform;
import com.moka.math.Vector2f;
import com.moka.math.Matrix3;

/**
 * Used to make calculations without creating a lot of
 * matrices and vectors, since it uses some private defined
 * objects.
 *
 * @author shelo
 */
public class CalcUtils
{
    private static final Matrix3 BUF_1_MAT = new Matrix3();
    private static final Matrix3 BUF_2_MAT = new Matrix3();
    private static final Matrix3 BUF_3_MAT = new Matrix3();

    private static final Vector2f BUF_1_VEC2 = new Vector2f();

    /**
     * Gets the model matrix from a transform, using static defined
     * matrices in order to reuse them when trying to achieve this operation.
     *
     * @param transform the transform where we'll get the model.
     * @return the model matrix.
     */
    public static Matrix3 calcModelMatrix(final Transform transform)
    {
        Vector2f position = transform.getPosition();

        Matrix3 translation = BUF_1_MAT.toTranslation((int) position.x, (int) position.y);
        Matrix3 scale = BUF_2_MAT.toScale(transform.getSize().x, transform.getSize().y);
        Matrix3 rotate = transform.getRotation();

        return translation.mul(rotate.mul(scale, BUF_3_MAT), BUF_3_MAT);
    }

    /**
     * Rotate a rotation matrix.
     *
     * @param original the matrix that will be rotated.
     * @param radians the amount of radians that we'll rotate the matrix.
     */
    public static void rotateMatrix(final Matrix3 original, float radians)
    {
        BUF_1_MAT.toRotation(radians);
        original.set(original.mul(BUF_1_MAT, BUF_2_MAT));
    }

    public static float calcFrontAngle(final Transform transform)
    {
        return transform.getFront(BUF_1_VEC2).angle();
    }
}
