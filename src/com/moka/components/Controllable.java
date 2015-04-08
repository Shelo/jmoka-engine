package com.moka.components;

import com.moka.core.Input;
import com.moka.core.subengines.Time;
import com.moka.core.xml.XmlAttribute;
import com.moka.core.xml.XmlSupported;
import com.moka.physics.Collision;
import org.lwjgl.glfw.GLFW;

@XmlSupported
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

		if (getInput().getKey(GLFW.GLFW_KEY_D))
		{
			tx += getTime().getDelta() * impulse;
		}

		if (getInput().getKey(GLFW.GLFW_KEY_A))
		{
			tx -= getTime().getDelta() * impulse;
		}

		if (!(getInput().getKey(GLFW.GLFW_KEY_D) || getInput().getKey(GLFW.GLFW_KEY_A)))
		{
			if (tx > -TOLERANCE && tx < TOLERANCE)
			{
				tx = 0;
			}
			else
			{
				tx += (tx < 0) ? getTime().getDelta() : 0 - getTime().getDelta();
			}
		}

		if (getInput().getKey(GLFW.GLFW_KEY_W))
		{
			ty += getTime().getDelta() * impulse;
		}

		if (getInput().getKey(GLFW.GLFW_KEY_S))
		{
			ty -= getTime().getDelta() * impulse;
		}

		if (!(getInput().getKey(GLFW.GLFW_KEY_W) || getInput().getKey(GLFW.GLFW_KEY_S)))
		{
			if (ty > -TOLERANCE && ty < TOLERANCE)
			{
				ty = 0;
			}
			else
			{
				ty += (ty < 0) ? getTime().getDelta() : 0 - getTime().getDelta();
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

		vx *= getTime().getDelta();
		vy *= getTime().getDelta();

		getTransform().move(vx, vy);
	}

	@Override
	public void onCollide(Collision collision)
	{
		if (Math.abs(tx) > Math.abs(ty))
		{
			tx = 0;
		}
		else
		{
			ty = 0;
		}
	}

	@XmlAttribute("topSpeed")
	public void setTopSpeed(int topSpeed)
	{
		this.topSpeed = topSpeed;
	}

	@XmlAttribute("acceleration")
	public void setAcceleration(int acceleration)
	{
		this.acceleration = acceleration;
	}

	@XmlAttribute("constrainX")
	public void setConstrainX(boolean constrainX)
	{
		this.constrainX = constrainX;
	}

	@XmlAttribute("constrainY")
	public void setConstrainY(boolean constrainY)
	{
		this.constrainY = constrainY;
	}

	@XmlAttribute("impulse")
	public void setImpulse(float impulse)
	{
		this.impulse = impulse;
	}
}
