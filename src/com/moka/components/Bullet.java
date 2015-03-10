package com.moka.components;

import com.moka.core.Time;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2f;
import com.moka.physics.Collision;

public class Bullet extends Component {
	private Vector2f direction = new Vector2f(1, 0);
	private Vector2f prevDir = new Vector2f();
	private Vector2f bufVector1 = new Vector2f();
	private Vector2f bufVector2 = new Vector2f();
	private float speed = 300;

	public Bullet() {
		
	}

	@Override
	public void onCreate() {
		direction.nor();
	}

	@Override
	public void onUpdate() {
		if(!direction.equals(prevDir))
			getTransform().setRotation(direction.angle());

		bufVector1.set(getTransform().getPosition());
		bufVector2.set(direction).mul((float) (speed * Time.getDelta()));
		getTransform().setPosition(bufVector1.add(bufVector2));

		prevDir.set(direction);
	}

	@Override
	public void onCollide(Collision collision) {
		/*if (collision.isGuilty()) {
			collision.entity.destroy();

			if (collision.entity.getName().startsWith("Bullet")) {
				getEntity().destroy();
			}
		}*/
	}

	@XmlAttribute("speed")
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setDirectionX(float d) {
		direction.x = d;
	}

	public void setDirectionY(float d) {
		direction.y = d;
	}

	public void setDirection(Vector2f direction) {
		this.direction.set(direction);
	}
}
