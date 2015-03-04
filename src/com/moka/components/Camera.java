package com.moka.components;

import com.moka.core.Moka;
import com.moka.core.Transform;
import com.moka.core.xml.XmlSupported;
import com.moka.exceptions.JMokaException;
import com.moka.math.Matrix4;
import com.moka.math.Quaternion;
import com.moka.math.Vector3;

@XmlSupported
public class Camera extends Component {
	public static final float Z_NEAR 	= -10;
	public static final float Z_FAR 	=  10;

	private Matrix4 projection;

	/**
	 * Constructor without parameters to work with XML initialization.
	 */
	public Camera() {
		projection = Matrix4.orthographic(0, Moka.getDisplay().getWidth(), 0, Moka.getDisplay().getHeight(),
				Z_NEAR, Z_FAR);
		setAsCurrent();
	}

	public Camera(float left, float right, float bottom, float top, float zNear, float zFar) {
		projection = Matrix4.orthographic(left, right, bottom, top, zNear, zFar);
	}

	@Override
	public void onUpdate(double delta) {

	}

	public void setAsCurrent() {
		Moka.getRenderer().setCamera(this);
	}

	public Matrix4 getProjectedViewMatrix() {
		if(projection == null)
			JMokaException.raise("Camera: " + getEntity().getName() + "'s projection is null.");

		Transform transform = getTransform();

		Vector3 position 	= transform.getPosition().mul(-1);
		Quaternion rotation = transform.getRotation().conjugate();

		Matrix4 translation = Matrix4.translate(position.getX(), position.getY(), position.getZ());
		Matrix4 rotate 		= rotation.toRotationMatrix();

		return projection.mul(rotate.mul(translation));
	}

	public Matrix4 getProjectionMatrix() {
		return projection;
	}
}
