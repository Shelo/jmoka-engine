package com.moka.components;

import com.moka.core.Time;
import com.moka.core.xml.XmlAttribute;
import com.moka.math.Vector2;
import com.moka.physics.Collision;

public class Bullet extends Component {
	private Vector2 direction = new Vector2(1, 0);
	private float speed = 300;

	public Bullet() {
		
	}

	@Override
	public void onCreate() {
		direction = direction.normalized();
	}

	@Override
	public void onUpdate() {
		getTransform().setPosition(getTransform().getPosition().add(
				direction.mul((float) (speed * Time.getDelta()))));
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

	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}
}
