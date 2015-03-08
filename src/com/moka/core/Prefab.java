package com.moka.core;

import com.moka.components.Component;
import com.moka.core.xml.XmlAttribute;
import com.moka.exceptions.JMokaException;
import com.moka.math.Vector2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

/**
 * Creates an prefabricated object, which can instantiate multiple entities using a simple
 * yet easy to use <b>state machine</b>. The prefab shouldn't be instantiated directly, but through
 * a {@link com.moka.core.xml.XmlPrefabReader}, with entityReader.newPrefab(filePath).
 * 
 * @author Shelo
 */
public class Prefab {
	private PreComponents components = new PreComponents();
	private BaseGame baseGame;
	private Vector2 position = Vector2.ZERO.copy();
	private float rotation;
	private Vector2 size;
	private int layer;

	public Prefab(BaseGame baseGame, PreComponents components) {
		this.components = components;
		this.baseGame = baseGame;
	}

	/**
	 * Sets the position for the new instances from now on.
	 *
	 * @param position position vector.
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}

	/**
	 * Sets the size from now on, you can always set it to null so the transform uses
	 * the size of the sprite.
	 * 
	 * @param size size vector.
	 */
	public void setSize(Vector2 size) {
		this.size = size;
	}

	/**
	 * Sets the rotation of new entities from now on, in degrees.
	 * 
	 * @param rotation angle.
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/**
	 * Sets the layer of new entities from now on.
	 * @param layer layer integer number.
	 */
	public void setLayer(int layer) {
		this.layer = layer;
	}

	/**
	 * Creates a new instance with using this prefab rules and states. The instance will
	 * be automatically added to the stage.
	 * 
	 * @param name unique name for the new instance.
	 * @return the entity.
	 */
	public Entity newEntity(String name) {
		Entity entity = baseGame.newEntity(name);

		// set transform values for this entity.
		Transform transform = entity.getTransform();
		transform.setPosition(position.copy());
		transform.setSize(size.copy());
		transform.setRotation(rotation);

		// creates every component and adds it to the entity.
		for (Class<?> cClass : components.keySet()) {
			ComponentAttrs componentAttrs = components.get(cClass);
			Component component = null;

			try {
				component = (Component) cClass.newInstance();

				// iterate over the methods and attributes of the component.
				for(Method method : componentAttrs.getKeySet()) {
					Object attr = componentAttrs.getValue(method);

					try {
						method.invoke(component, attr);
					} catch(InvocationTargetException e) {
						throw new JMokaException("Cannot set the attribute "
								+ method.getAnnotation(XmlAttribute.class).value());
					}
				}

			} catch(InstantiationException e) {
				throw new JMokaException("Cannot instantiate the component " + cClass.getName()
						+ ", maybe there's no non-args constructor.");
			} catch(IllegalAccessException e) {
				throw new JMokaException("Cannot access the component " + cClass.getName() + ".");
			}

			entity.addComponent(component);
		}

		// after this we should call the onCreate method on the components.
		entity.create();

		return entity;
	}

	/**
	 * This is just a hack so I don't have to write that big HashMap every time.
	 */
	public static class PreComponents extends HashMap<Class<?>, ComponentAttrs> {

	}

	/**
	 * Wrapper and helper class to the prefab.
	 */
	public static class ComponentAttrs {
		private HashMap<Method, Object> methodValues = new HashMap<>();

		public void addMethodValue(Method method, Object value) {
			methodValues.put(method, value);
		}

		public Object getValue(Method name) {
			return methodValues.get(name);
		}

		public Set<Method> getKeySet() {
			return methodValues.keySet();
		}
	}
}
