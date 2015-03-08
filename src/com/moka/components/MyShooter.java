package com.moka.components;

import com.moka.core.Entity;
import com.moka.core.Input;
import com.moka.core.Prefab;
import com.moka.math.Vector2;
import org.lwjgl.glfw.GLFW;

public class MyShooter extends Component {
	private Prefab bullet;
	private int counter;

	@Override
	public void onCreate() {
		bullet = newPrefab("res/scene/entities/bullet.xml");
	}

	@Override
	public void onUpdate() {
		if (Input.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
			Vector2 dir = Input.getCursorPos().sub(getTransform().getPosition());
			dir = dir.normalized();
			bullet.setRotation((float) Math.toDegrees(dir.angle()));
			bullet.setPosition(getTransform().getPosition());
			Entity entity = bullet.newEntity("Bullet" + (counter++));
			Bullet bullet1 = entity.getComponent(Bullet.class);
			bullet1.setDirection(dir);
		}
	}
}
