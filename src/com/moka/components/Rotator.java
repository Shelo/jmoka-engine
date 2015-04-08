package com.moka.components;

import com.moka.core.Time;

public class Rotator extends Component {
	private float angle;

	@Override
	public void onUpdate() {
		getTransform().setRotationDeg(angle += (float) (Time.getDelta() * 90));
	}
}
