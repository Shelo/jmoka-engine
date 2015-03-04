package com.moka.core;

import com.moka.components.Component;
import com.moka.components.Sprite;
import com.moka.graphics.Shader;
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
		components.add(component);
		return this;
	}

	public void onCreate() {
		for(Component component : components)
			component.onCreate();
	}

	public void render(Shader shader) {
		for(Component component : components)
			component.onRender(shader);
	}

	public void update(double delta) {
		for(Component component : components)
			component.onUpdate(delta);
	}

	public Transform getTransform() {
		return transform;
	}

	public String getName() {
		return name;
	}
	
	public <T> T getComponent(Class<T> componentClass) {
		for(Component component : components)
			if(componentClass.isInstance(component))
				return componentClass.cast(component);
		return null;
	}
}
