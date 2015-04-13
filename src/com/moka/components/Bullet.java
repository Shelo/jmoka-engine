package com.moka.components;

import com.moka.core.Component;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2f;
import com.moka.physics.Collision;

public class Bullet extends Component
{
	private Vector2f buffer = new Vector2f();
	private float speed = 300;

	public Bullet()
	{
		// XML Support.
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onUpdate()
	{
		buffer.set(getTransform().getFront(buffer)).mul((float) (speed * getTime().getDelta()));
		getTransform().move(buffer);
	}

	@Override
	public void onCollide(Collision collision)
	{

	}

	@XmlAttribute("speed")
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
}
