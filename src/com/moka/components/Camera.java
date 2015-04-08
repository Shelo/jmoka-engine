package com.moka.components;

import com.moka.core.Transform;
import com.moka.core.xml.XmlSupported;
import com.moka.exceptions.JMokaException;
import com.moka.math.Matrix4;
import com.moka.math.Quaternion;

@XmlSupported
public class Camera extends Component
{
	public static final float Z_NEAR 	= -10;
	public static final float Z_FAR 	=  10;

	private Matrix4 projection;
	private Matrix4 translationMat = new Matrix4();
	private Matrix4 rotationMat = new Matrix4();
	private Matrix4 mulBuffer = new Matrix4();

	public Camera()
	{

	}

	@Override
	public void onCreate()
	{
		projection = Matrix4.orthographic(0, getDisplay().getWidth(), 0, getDisplay().getHeight(),
				Z_NEAR, Z_FAR);
		setAsCurrent();
	}

	public Camera(float left, float right, float bottom, float top, float zNear, float zFar)
	{
		projection = Matrix4.orthographic(left, right, bottom, top, zNear, zFar);
	}

	public void setAsCurrent()
	{
		getApplication().getRenderer().setCamera(this);
	}

	public Matrix4 getProjectedViewMatrix()
	{
		if(projection == null)
		{
			throw new JMokaException("Camera: " + getEntity().getName() + "'s projection is null.");
		}

		Transform t = getTransform();

		Quaternion rotation = t.getRotation().conjugate();
		Matrix4 translation = translationMat.toTranslation(t.getPositionX() * -1,
				t.getPositionY() * -1, t.getPositionZ() * -1);
		Matrix4 rotate = rotation.toRotationMatrix(rotationMat);
		rotate.mul(translation, mulBuffer);
		return projection.mul(mulBuffer, mulBuffer);
	}

	public Matrix4 getProjectionMatrix()
	{
		return projection;
	}
}
