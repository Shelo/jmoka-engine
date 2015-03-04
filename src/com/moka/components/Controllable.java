package com.moka.components;

import com.moka.core.Input;
import com.moka.core.Time;
import com.moka.core.Timer;
import com.moka.core.xml.XmlAttribute;
import com.moka.core.xml.XmlSupported;
import org.lwjgl.glfw.GLFW;

@XmlSupported
public class Controllable extends Component {
	public static final float TOLERANCE = 0.03f;

	// Xml Attributes.
	private boolean constrainX	= false;
	private boolean constrainY	= false;
	private int acceleration	= 100;
	private int topSpeed		= 500;

	private float speedX;
	private float speedY;

	private float tx, ty;
	private float impulse = 2;

	Timer timer;


	public Controllable() { }

	@Override
	public void onCreate() {
		timer = Time.newTimer();
	}

	@Override
	public void onUpdate() {
		if (Input.getKey(GLFW.GLFW_KEY_D))
			tx += Time.getDelta() * impulse;

		if (Input.getKey(GLFW.GLFW_KEY_A))
			tx -= Time.getDelta() * impulse;

		if (!(Input.getKey(GLFW.GLFW_KEY_D) || Input.getKey(GLFW.GLFW_KEY_A))) {
			if (tx > -TOLERANCE && tx < TOLERANCE)
				tx = 0;

			else
				tx += (tx < 0) ? Time.getDelta() : 0 - Time.getDelta();
		}

		if (Input.getKey(GLFW.GLFW_KEY_W))
			ty += Time.getDelta() * impulse;

		if (Input.getKey(GLFW.GLFW_KEY_S))
			ty -= Time.getDelta() * impulse;

		if (!(Input.getKey(GLFW.GLFW_KEY_W) || Input.getKey(GLFW.GLFW_KEY_S))) {
			if (ty > -TOLERANCE && ty < TOLERANCE)
				ty = 0;

			else
				ty += (ty < 0) ? Time.getDelta() : 0 - Time.getDelta();
		}

		float vx = acceleration * tx;
		float vy = acceleration * ty;

		if (Math.abs(vx) > topSpeed)
			vx = (vx < 0) ? 0 - topSpeed : topSpeed;

		if (Math.abs(vy) > topSpeed)
			vy = (vy < 0) ? 0 - topSpeed : topSpeed;

		vx *= Time.getDelta();
		vy *= Time.getDelta();

		getTransform().move(vx, vy);

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

	@XmlAttribute("impulse")
	public void setImpulse(float impulse) {
		this.impulse = impulse;
	}
}
