package com.moka.core;

import com.moka.components.Component;
import com.moka.components.Sprite;
import com.moka.physics.Collider;

import java.util.ArrayList;

public class Entity {
	private final ArrayList<Component> components;
	private final Transform transform;
	private final String name;

	private Collider collider;
	private Sprite sprite;

	public Entity(String name) {
		this.name = name;

		components 	= new ArrayList<>();
		transform 	= new Transform();
	}

	public Entity addComponent(Component component) {
		component.setEntity(this);
		if(component instanceof Sprite)
			sprite = (Sprite) component;
		else
			components.add(component);
		return this;
	}

	public void create() {
		for(Component component : components)
			component.onCreate();
	}

	public void update(double delta) {
		for(Component component : components)
			component.onUpdate(delta);
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

	public Sprite getSprite() {
		return sprite;
	}

	public Collider getCollider() {
		return collider;
	}

	public String getName() {
		return name;
	}

	public boolean hasSprite() {
		return sprite != null;
	}
	
	public boolean hasCollider() {
		return collider != null;
	}
}
