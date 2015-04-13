package com.moka.components;

import com.moka.core.Component;
import com.moka.core.Entity;
import com.moka.core.Transform;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2f;

public class LookAt extends Component
{
	private Vector2f buffer = new Vector2f();

	private Entity target;
	private Transform transform;

	@Override
	public void onCreate()
	{
		transform = getTransform();
	}

	@Override
	public void onUpdate()
	{
		Vector2f position = target.getTransform().getPosition();
		buffer.set(position);
		buffer.sub(transform.getPosition());
		transform.setRotation(buffer.angle());
	}

	@XmlAttribute("target")
	public void setTarget(Entity target)
	{
		this.target = target;
	}
}
