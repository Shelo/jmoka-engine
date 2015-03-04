package com.moka.core;

import com.moka.components.Component;
import com.moka.core.game.BaseGame;
import com.moka.exceptions.JMokaException;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The EntityFactory is a fast way to create prefabricated Entities and
 * avoid boilerplate repetitive code.
 * @author Shelo
 */
public final class EntityFactory {
	private HashMap<Class<? extends Component>, Arguments> arguments;
	private ArrayList<Class<? extends Component>> components;
	private ArrayList<Component> inListComponents;

	private EntityFactory(Builder builder) {
		inListComponents 	= new ArrayList<>();
		components 			= new ArrayList<>();
		arguments 			= new HashMap<>();

		// copy the list of components and arguments.
		for(Class<? extends Component> component : builder.components) {
			components.add(component);

			if(builder.arguments.containsKey(component))
				arguments.put(component, builder.arguments.get(component));
		}

		// copy inListComponents.
		for(Component inListComponent : builder.inListComponents)
			inListComponents.add(inListComponent);
	}

	/**
	 * Creates a new entity using {@link com.moka.core.game.BaseGame}'s newEntity method, which
	 * means that the entity is added automatically to the game's hierarchy.
	 * @param game the current game.
	 * @return the new entity.
	 */
	public Entity newEntity(@NotNull BaseGame game, String tag) {
		if(game == null)
			JMokaException.raise("EntityFactory.newEntity: Game parameter must be not null.");

		assert game != null;
		Entity entity = game.newEntity(tag);

		for(Class<? extends Component> componentClass : components) {
			try {
				Component component = componentClass.newInstance();

				// add this component.
				entity.addComponent(component);

			} catch(InstantiationException e) {
				JMokaException.raise("Component " + componentClass.toString() + " not found.");
			} catch(IllegalAccessException e) {
				JMokaException.raise("Component " + componentClass.toString() + " is not accessible.");
			}
		}

		for(Component inListComponent : inListComponents)
			entity.addComponent(inListComponent);

		return entity;
	}

	/**
	 * Nested class designed to avoid unchecked warnings. This class follows the Builder Pattern.
	 * After building, you can safely add or remove components in it, without altering the previously created
	 * factories.<br>
	 * <b>Usage:</b><br>
	 * {@code EntityFactory factory = new EntityFactory.Builder().add(DummyComponent.class).build();}
	 */
	public static class Builder {
		private HashMap<Class<? extends Component>, Arguments> arguments;
		private ArrayList<Class<? extends Component>> components;
		private ArrayList<Component> inListComponents;

		private String name;

		public Builder() {
			inListComponents 	= new ArrayList<>();
			components 			= new ArrayList<>();
			arguments 			= new HashMap<>();
		}

		/**
		 * Adds a component to the component list.
		 * @param component	a class that extends {@link com.moka.components.Component};
		 * @return this builder for chaining.
		 */
		public Builder add(Class<? extends Component> component) {
			if(!components.contains(component))
				components.add(component);
			else
				JMokaException.raise("EntityFactory.Builder: repeated component: " + component.toString() + "-");
			return this;
		}

		/**
		 * Adds a component to the component list within an {@link com.moka.core.Arguments} object.
		 * @param component	a class that extends {@link com.moka.components.Component}.
		 * @param arguments arguments applied to this component.
		 * @return this builder for chaining.
		 */
		public Builder add(Class<? extends Component> component, Arguments arguments) {
			// add component and arguments, this process will be terminated if in the following line
			// the component was already added.
			add(component);
			this.arguments.put(component, arguments);
			return this;
		}

		public Builder add(Component component) {
			if(component == null)
				JMokaException.raise("Component can't be null.");
			inListComponents.add(component);
			return this;
		}

		/**
		 * Removes a component, if the component didn't exists, no exception is raised.
		 * @param component the component that will be removed.
		 * @return this builder for chaining.
		 */
		public Builder remove(Class<? extends Component> component) {
			components.remove(component);
			return this;
		}

		/**
		 * Builds the final {@link com.moka.core.EntityFactory}.
		 * @return the {@link com.moka.core.EntityFactory}.
		 */
		public EntityFactory build() {
			return new EntityFactory(this);
		}

		public String getName() {
			return name;
		}
	}
}
