package com.moka.components;

import com.moka.core.Entity;
import com.moka.core.Moka;
import com.moka.core.Transform;
import com.moka.exceptions.JMokaException;
import com.moka.graphics.Shader;

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

	/**
	 * Called at the creation time.
	 */
	public void onCreate() { }

	/**
	 * Called every update frame.
	 * @param delta the current delta time.
	 */
	public void onUpdate(final double delta) { }

	/**
	 * Called every render frame.
	 * @param shader shader to render with.
	 */
	public void onRender(Shader shader) { }
}
