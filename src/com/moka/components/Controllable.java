package com.moka.components;

import com.moka.core.Input;
import com.moka.core.xml.XmlAttribute;
import com.moka.core.xml.XmlSupported;
import org.lwjgl.glfw.GLFW;

@XmlSupported
public class Controllable extends Component {
	// Xml Attributes.
	private boolean constrainX	= false;
	private boolean constrainY	= false;
	private int acceleration	= 100;
	private int topSpeed		= 500;

	private float speedX;
	private float speedY;

	public Controllable() { }

	@Override
	public void onUpdate(final double delta) {
		float dx = 0;
		float dy = 0;

		/*
		if(!constrainX) {
			if(Input.getKey(GLFW.GLFW_KEY_D)) {
				speedX += acceleration;
			} else if(Input.getKey(GLFW.GLFW_KEY_A)) {
				speedX -= acceleration;
			} else {
				speedX = 0;
			}

			speedX = MokaMath.clamp(speedX, - topSpeed, topSpeed);
			dx = (float) (speedX * delta);
		}

		if(!constrainY) {
			if(Input.getKey(GLFW.GLFW_KEY_W)) {
				speedY += acceleration;
			} else if(Input.getKey(GLFW.GLFW_KEY_S)) {
				speedY -= acceleration;
			} else {
				speedY = 0;
			}

			speedY = MokaMath.clamp(speedY, - topSpeed, topSpeed);
			dy = (float) (speedY * delta);
		}
		*/

		if(Input.getKey(GLFW.GLFW_KEY_D))
			dx = (float) (delta * 100);

		if(Input.getKey(GLFW.GLFW_KEY_A))
			dx = (float) (- delta * 100);
		
		if(Input.getKey(GLFW.GLFW_KEY_W))
			dy = (float) (delta * 100);

		if(Input.getKey(GLFW.GLFW_KEY_S))
			dy = (float) (- delta * 100);

		getTransform().move(dx, dy, 0);
	}

	@XmlAttribute("topSpeed")
	public void setTopSpeed(int topSpeed) {
		this.topSpeed = topSpeed;
	}

	@XmlAttribute("acceleration")
	public void setAcceleration(int acceleration) {
		this.acceleration = acceleration;
	}

	@XmlAttribute("constrainX")
	public void setConstrainX(boolean constrainX) {
		this.constrainX = constrainX;
	}

	@XmlAttribute("constrainY")
	public void setConstrainY(boolean constrainY) {
		this.constrainY = constrainY;
	}
}
