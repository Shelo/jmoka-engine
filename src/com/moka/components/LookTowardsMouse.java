package com.moka.components;

import com.moka.core.Transform;
import com.moka.math.Vector2f;

public class LookTowardsMouse extends Component
{
	private Vector2f cursorPosition = new Vector2f();
	private Transform transform;

	@Override
	public void onCreate()
	{
		transform = getTransform();
	}

	@Override
	public void onUpdate()
	{
		cursorPosition.set(getInput().getCursorPos());
		cursorPosition.sub(transform.getPosition());
		transform.setRotation(cursorPosition.angle());
	}
}
