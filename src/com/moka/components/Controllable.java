package com.moka.components;

import com.moka.core.Moka;
import com.moka.core.entity.Component;
import com.moka.core.ComponentAttribute;
import org.lwjgl.glfw.GLFW;

public class Controllable extends Component
{
	public static final float TOLERANCE = 0.03f;

	// Xml Attributes.
	private boolean constrainX	= false;
	private boolean constrainY	= false;
	private int acceleration	= 600;
	private int topSpeed		= 500;

	private float tx, ty;
	private float impulse = 2;

	public Controllable()
	{

	}

	@Override
	public void onCreate()
	{
		
	}

	@Override
	public void onUpdate()
	{
		float ltx = tx;
		float lty = ty;

		if (Moka.getInput().getKey(GLFW.GLFW_KEY_D))
		{
			tx += Moka.getTime().getDelta() * impulse;
		}

		if (Moka.getInput().getKey(GLFW.GLFW_KEY_A))
		{
			tx -= Moka.getTime().getDelta() * impulse;
		}

		if (!(Moka.getInput().getKey(GLFW.GLFW_KEY_D) || Moka.getInput().getKey(GLFW.GLFW_KEY_A)))
		{
			if (tx > -TOLERANCE && tx < TOLERANCE)
			{
				tx = 0;
			}
			else
			{
				tx += (tx < 0) ? Moka.getTime().getDelta() : 0 - Moka.getTime().getDelta();
			}
		}

		if (Moka.getInput().getKey(GLFW.GLFW_KEY_W))
		{
			ty += Moka.getTime().getDelta() * impulse;
		}

		if (Moka.getInput().getKey(GLFW.GLFW_KEY_S))
		{
			ty -= Moka.getTime().getDelta() * impulse;
		}

		if (!(Moka.getInput().getKey(GLFW.GLFW_KEY_W) || Moka.getInput().getKey(GLFW.GLFW_KEY_S)))
		{
			if (ty > -TOLERANCE && ty < TOLERANCE)
			{
				ty = 0;
			}
			else
			{
				ty += (ty < 0) ? Moka.getTime().getDelta() : 0 - Moka.getTime().getDelta();
			}
		}

		float vx = acceleration * tx;
		float vy = acceleration * ty;

		if (Math.abs(vx) > topSpeed)
		{
			vx = (vx < 0) ? 0 - topSpeed : topSpeed;
			tx = ltx;
		}

		if (Math.abs(vy) > topSpeed)
		{
			vy = (vy < 0) ? 0 - topSpeed : topSpeed;
			ty = lty;
		}

		vx *= Moka.getTime().getDelta();
		vy *= Moka.getTime().getDelta();

		getTransform().move(vx, vy);
	}

	@ComponentAttribute("TopSpeed")
	public void setTopSpeed(int topSpeed)
	{
		this.topSpeed = topSpeed;
	}

	@ComponentAttribute("Acceleration")
	public void setAcceleration(int acceleration)
	{
		this.acceleration = acceleration;
	}

	@ComponentAttribute("ConstrainX")
	public void setConstrainX(boolean constrainX)
	{
		this.constrainX = constrainX;
	}

	@ComponentAttribute("ConstrainY")
	public void setConstrainY(boolean constrainY)
	{
		this.constrainY = constrainY;
	}

	@ComponentAttribute("Impulse")
	public void setImpulse(float impulse)
	{
		this.impulse = impulse;
	}
}
