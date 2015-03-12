package com.moka.core;

import com.moka.math.Matrix4;
import com.moka.math.Vector2f;

public class CoreUtils {
	private static final Matrix4 TRANSLATION_MAT = new Matrix4();
	private static final Matrix4 ROTATION_MAT = new Matrix4();
	private static final Matrix4 MUL_BUFFER = new Matrix4();
	private static final Matrix4 SCALE_MAT = new Matrix4();

	/**
	 * Gets the model matrix from a transform. Utility used to reuse matrices.
	 * @param transform the transform.
	 * @return the model matrix.
	 */
	public static Matrix4 getModelMatrix(Transform transform) {
		Vector2f position = transform.getPosition();
		Matrix4 translation = TRANSLATION_MAT.toTranslation((int) position.x, (int) position.y, 0);
		Matrix4 scale = SCALE_MAT.toScale(transform.getSize().x, transform.getSize().y, 1);
		Matrix4 rotate = transform.getRotation().toRotationMatrix(ROTATION_MAT);
		return translation.mul(rotate.mul(scale, MUL_BUFFER), MUL_BUFFER);
	}
}
