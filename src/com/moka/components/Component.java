package com.moka.components;

import com.moka.core.Entity;
import com.moka.core.Moka;
import com.moka.core.Transform;
import com.moka.exceptions.JMokaException;
import com.moka.physics.Collision;

/**
 * Component class, establishes the common way a component should behave.
 * Class designed for inheritance.
 * @author Shelo
 */
public abstract class Component {
	private Entity entity;

	public Component() {
		
	}

	public final void setEntity(final Entity entity) {
		if(this.entity != null)
			JMokaException.raise("This component already has an entity.");
		this.entity = entity;
	}

	public final Transform getTransform() {
		return entity.getTransform();
	}

	public final Entity getEntity() {
		return entity;
	}

	public final Entity findEntity(String tag) {
		return Moka.getGame().findEntity(tag);
	}

	public final <T> T getComponent(Class<T> componentClass) {
		return entity.getComponent(componentClass);
	}

	/**
	 * Called at the creation time.
	 */
	public void onCreate() { }

	/**
	 * Called every update frame.
	 */
	public void onUpdate() { }

	/**
	 * Called when the entity collided with another entity.
	 * @param collision the collision information.
	 */
	public void onCollide(Collision collision) { }

	/**
	 * Called just after the physics system resolved every collision.
	 */
	public void onPostUpdate() { }
}
