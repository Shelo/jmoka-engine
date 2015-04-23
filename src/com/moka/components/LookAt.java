package com.moka.components;

import com.moka.core.Component;
import com.moka.core.Entity;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2;
import com.moka.utils.Pools;

public class LookAt extends Component
{
	private Entity target;

	@Override
	public void onUpdate()
	{
		Vector2 buffer = Pools.vector2.take();

		buffer.set(target.getTransform().getPosition());
		buffer.sub(getTransform().getPosition());
		getTransform().setRotation(buffer.angle());

		Pools.vector2.put(buffer);
	}

	@XmlAttribute(value = "target", required = true)
	public void setTarget(Entity target)
	{
		this.target = target;
	}
}
