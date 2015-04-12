package com.moka.utils;

import com.moka.core.Transform;
import com.moka.math.Vector2f;
import com.moka.math.Matrix3;

public class CoreUtils
{
    private static final Matrix3 TRANS_MAT  = new Matrix3();
    private static final Matrix3 SCL_MAT    = new Matrix3();
    private static final Matrix3 BUF_MAT    = new Matrix3();

    /**
     * Gets the model matrix from a transform, using static defined
     * matrices in order to reuse them when trying to achieve this operation.
     *
     * @param transform the transform where we'll get the model.
     * @return the model matrix.
     */
    public static Matrix3 calcModelMatrix(Transform transform)
    {
        Vector2f position = transform.getPosition();

        Matrix3 translation = TRANS_MAT.toTranslation((int) position.x, (int) position.y);
        Matrix3 scale = SCL_MAT.toScale(transform.getSize().x, transform.getSize().y);
        Matrix3 rotate = transform.getRotation();

        return translation.mul(rotate.mul(scale, BUF_MAT), BUF_MAT);
    }
}
