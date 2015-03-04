package com.moka.components;

import com.moka.core.Time;

public class Rotator extends Component {
	private float angle;

	@Override
	public void onUpdate() {
		getTransform().setRotation(angle += (float) (Time.getDelta() * 90));
	}
}
