package com.moka.components;

import com.moka.math.Vector2f;
import com.moka.math.Matrix3;
import com.moka.utils.JMokaException;

public class Camera extends Component
{
	private Matrix3 projection;

	private Matrix3 transBuffer = new Matrix3();
	private Matrix3 buffer = new Matrix3();

	public Camera()
	{

	}

	@Override
	public void onCreate()
	{
		projection = new Matrix3();
		projection.toOrthographic(0, getDisplay().getWidth(), 0, getDisplay().getHeight());

		// TODO: this shouldn't be here.
		setAsCurrent();
	}

	public Camera(float left, float right, float bottom, float top)
	{
		projection = new Matrix3();
		projection.toOrthographic(left, right, bottom, top);
	}

	public void setAsCurrent()
	{
		getApplication().getRenderer().setCamera(this);
	}

	public Matrix3 getProjectedView()
	{
		if(projection == null)
		{
			throw new JMokaException("Camera: " + getEntity().getName() + "'s projection is null.");
		}

		Vector2f position = getTransform().getPosition();
		Matrix3 translation = transBuffer.toTranslation(position.x * (- 1), position.y * (- 1));
		return projection.mul(translation, buffer);
	}

	public Matrix3 getProjection()
	{
		return projection;
	}
}
