package com.moka.components;

import com.moka.core.Moka;
import com.moka.math.Matrix3;
import com.moka.math.Vector2;
import com.moka.scene.entity.Component;
import com.moka.utils.JMokaException;
import com.moka.utils.Pools;

public class Camera extends Component
{
	private Matrix3 projection;
	private Matrix3 transBuffer = new Matrix3();
	private Matrix3 buffer = new Matrix3();

	@Override
	public void onCreate()
	{
		projection = new Matrix3();
		projection.toOrthographic(0, Moka.getDisplay().getWidth(), 0, Moka.getDisplay().getHeight());
	}

	public Camera(float left, float right, float bottom, float top)
	{
		projection = new Matrix3();
		projection.toOrthographic(left, right, bottom, top);
	}

	public void setAsMain()
	{
		Moka.getRenderer().setCamera(this);
	}

	public Matrix3 getProjectedView()
	{
		if(projection == null)
			throw new JMokaException("Camera: " + getEntity().getName() + "'s projection is null.");

		Vector2 position = Pools.vec2.take();
		getTransform().getPosition().floor(position);
		Matrix3 translation = transBuffer.toTranslation(position.x * (- 1), position.y * (- 1));
		return projection.mul(translation, buffer);
	}

	public Matrix3 getProjection()
	{
		return projection;
	}

	/**
	 * Converts the given point to world coordinates, applying the camera transformations.
	 *
	 * @param point		the point that will be translated.
	 * @param result	the resulting vector (see {@link com.moka.utils.Pools.vec2})
	 * @return the transformed point.
	 */
	public Vector2 toWorldCoords(Vector2 point, Vector2 result)
	{
        // TODO: this is not ready for rotation.
		result.set(point);
		result.add(getTransform().getPosition());
		return result;
	}
}
