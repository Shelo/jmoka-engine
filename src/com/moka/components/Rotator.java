package com.moka.components;

import com.moka.core.Moka;

public class Rotator extends Component {
	private float angle;

	@Override
	public void onUpdate(double delta) {
		getTransform().setRotation(angle += (float) (Moka.getDelta() * 90));
	}
}
