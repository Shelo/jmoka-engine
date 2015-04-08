package com.moka.components;

public class Rotator extends Component
{
	private float angle;

	@Override
	public void onUpdate()
	{
		getTransform().setRotationDeg(angle += (float) (getTime().getDelta() * 90));
	}
}
