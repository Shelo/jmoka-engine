package com.moka.components;

import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2f;
import com.moka.physics.Collision;

public class Bullet extends Component {
	private Vector2f direction = new Vector2f(1, 0);
	private Vector2f prevDir = new Vector2f();
	private Vector2f buffer = new Vector2f();
	private float speed = 300;

	public Bullet()
	{
		// XML Support.
	}

	@Override
	public void onCreate()
	{
		direction.nor();
	}

	@Override
	public void onUpdate()
	{
		// change rotation only if the direction changed.
		if(!direction.equals(prevDir))
		{
			getTransform().setRotation(direction.angle());
		}

		// save the distance that we should move this frame to a buffer.
		buffer.set(direction).mul((float) (speed * getTime().getDelta()));

		// add the said distance.
		getTransform().move(buffer);

		// save the direction to the previous direction.
		prevDir.set(direction);
	}

	@Override
	public void onCollide(Collision collision)
	{

	}

	@XmlAttribute("speed")
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	@XmlAttribute("directionX")
	public void setDirectionX(float d)
	{
		direction.x = d;
	}

	@XmlAttribute("directionY")
	public void setDirectionY(float d)
	{
		direction.y = d;
	}

	public void setDirection(Vector2f direction)
	{
		this.direction.set(direction);
	}
}
