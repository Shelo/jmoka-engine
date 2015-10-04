package com.moka.components;

import com.moka.math.Vector2;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.utils.Pools;

public class LookAt extends Component
{
	private Vector2 target = Vector2.ZERO.cpy();

	@Override
	public void onUpdate()
	{
		Vector2 buffer = Pools.vec2.take(0, 0);
		{
			buffer.set(target);
			buffer.sub(getTransform().getPosition());
			getTransform().setRotation(buffer.angle());
		}
		Pools.vec2.put(buffer);
    }

	@ComponentAttribute("Target")
	public void setTarget(Vector2 target)
	{
		this.target = target;
	}
}
