package com.moka.components;

import com.moka.core.Input;
import com.moka.core.Transform;
import com.moka.math.Vector2;

public class LookTowardsMouse extends Component {
	Transform transform;

	@Override
	public void onCreate() {
		transform = getTransform();
	}

	@Override
	public void onUpdate() {
		Vector2 direction = Input.getCursorPos().sub(transform.getPosition());
		transform.setRotRadians(direction.angle());
	}
}
