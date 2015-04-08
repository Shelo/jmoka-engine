package example.components;

import com.moka.components.Bullet;
import com.moka.components.Component;
import com.moka.core.Entity;
import com.moka.core.Input;
import com.moka.core.Prefab;
import com.moka.math.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MyShooter extends Component {
	private Vector2f buf = new Vector2f();
	private Prefab bullet;
	private int counter;

	@Override
	public void onCreate() {
		bullet = newPrefab("res/scene/entities/bullet.xml");
	}

	@Override
	public void onUpdate() {
		if (Input.getMouseDown(GLFW.GLFW_MOUSE_BUTTON_1)) {
			buf.set(Input.getCursorPos());
			Vector2f dir = buf.sub(getTransform().getPosition()).nor();

			bullet.setPosition(getTransform().getPosition());
			Entity entity = bullet.newEntity("Bullet" + (counter++));

			Bullet bullet1 = entity.getComponent(Bullet.class);
			bullet1.setDirection(dir);
		}
	}
}
