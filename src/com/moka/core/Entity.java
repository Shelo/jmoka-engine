package com.moka.core;

import com.moka.components.Component;
import com.moka.components.Sprite;
import com.moka.math.Matrix4;
import com.moka.math.Vector2;
import com.moka.physics.Collider;
import com.moka.physics.Collision;

import java.util.ArrayList;

public class Entity {
	private final ArrayList<Component> components;
	private final Transform transform;
	private final String name;

	private Collider collider;
	private Sprite sprite;

	public Entity(String name) {
		this.name = name;

		components = new ArrayList<>();
		transform = new Transform(this);
	}

	public Entity addComponent(Component component) {
		component.setEntity(this);
		if(component instanceof Sprite)
			sprite = (Sprite) component;
		else if(component instanceof Collider)
			collider = (Collider) component;
		else
			components.add(component);
		return this;
	}

	public void create() {
		if(hasSprite()) sprite.onCreate();
		if(hasCollider()) collider.onCreate();

		for(Component component : components)
			component.onCreate();
	}

	public void update(double delta) {
		transform.update();
		for(Component component : components)
			component.onUpdate(delta);
	}

	public void collide(Collision collision) {
		for(Component component : components)
			component.onCollide(collision);
	}

	public void postUpdate() {
		for(Component component : components)
			component.onPostUpdate();
	}

	public <T> T getComponent(Class<T> componentClass) {
		for(Component component : components)
			if(componentClass.isInstance(component))
				return componentClass.cast(component);
		return null;
	}

	public Transform getTransform() {
		return transform;
	}

	public Collider getCollider() {
		return collider;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public String getName() {
		return name;
	}

	public Vector2[] transformVertices(Vector2[] vertices) {
		Vector2[] res = new Vector2[vertices.length];
		Matrix4 model = getTransform().getModelMatrix();

		for(int i = 0; i < vertices.length; i++)
			res[i] = model.mul(vertices[i]);

		return res;
	}

	public boolean hasSprite() {
		return sprite != null;
	}

	public boolean hasCollider() {
		return collider != null;
	}
}
