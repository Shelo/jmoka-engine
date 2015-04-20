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

	@Override
	public void onUpdate()
	{
		buffer.set(target.getTransform().getPosition());
		buffer.sub(getTransform().getPosition());
		getTransform().setRotation(buffer.angle());
	}

	@XmlAttribute(value = "target", required = true)
	public void setTarget(Entity target)
	{
		System.out.println(target.getName());
		this.target = target;
	}
}
