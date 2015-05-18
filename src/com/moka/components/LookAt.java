package com.moka.components;

import com.moka.core.entity.Component;
import com.moka.core.entity.Entity;
import com.moka.core.readers.ComponentAttribute;
import com.moka.math.Vector2;
import com.moka.utils.Pools;

public class LookAt extends Component
{
	private Entity target;

	@Override
	public void onUpdate()
	{
		Vector2 buffer = Pools.vec2.take(0, 0);

		buffer.set(target.getTransform().getPosition());
		buffer.sub(getTransform().getPosition());
		getTransform().setRotation(buffer.angle());

		Pools.vec2.put(buffer);
	}

	@ComponentAttribute(value = "target", required = true)
	public void setTarget(Entity target)
	{
		this.target = target;
	}
}
