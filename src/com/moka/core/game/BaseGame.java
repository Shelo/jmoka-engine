package com.moka.core.game;

import com.moka.components.Camera;
import com.moka.core.Entity;
import com.moka.core.Moka;
import com.moka.core.xml.XmlEntityReader;
import com.moka.core.xml.XmlSceneReader;
import com.moka.exceptions.JMokaException;
import com.moka.graphics.Shader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base class of a game, takes care of internal core usage.
 */
public abstract class BaseGame {
	private HashMap<String, Entity> nameRelations 	= new HashMap<>();
	private ArrayList<Entity> entities 				= new ArrayList<>();

	private XmlEntityReader entityReader;
	private XmlSceneReader sceneReader;

	public BaseGame() {
		sceneReader = new XmlSceneReader(this);
		entityReader = sceneReader.getEntityReader();
	}

	public final void onUpdateAll(double delta) {
		for(Entity entity : entities)
			entity.update(delta);
	}

	public final void onCreateAll() {
		for(Entity entity : entities)
			entity.create();
	}

	public final void onRenderAll(Shader shader) {
		for(Entity entity : entities)
			if(entity.hasSprite())
				entity.getSprite().render(shader);
	}

	public final Entity addEntity(Entity entity) {
		entities.add(entity);
		return entity;
	}

	/**
	 * Constructs a new {@link com.moka.core.Entity} and add it to the hierarchy.
	 * @return the new {@link com.moka.core.Entity}.
	 */
	public final Entity newEntity(String name) {
		// throw exception if the name already exists.
		if(nameRelations.containsKey(name))
			throw new JMokaException("Entity with name " + name + " already exists.");

		// create and add the entity to the game.
		Entity entity = new Entity(name);
		nameRelations.put(name, entity);
		addEntity(entity);
		return entity;
	}

	/**
	 * Creates a new entity with a camera on it.
	 * @param name		name for the entity.
	 * @param current	sets this camera as the current one.
	 * @return			the entity.
	 */
	public Entity newCamera(String name, boolean current) {
		Entity entity = newEntity(name);
		Camera camera = new Camera(0, Moka.getDisplay().getWidth(), 0, Moka.getDisplay().getHeight(), Camera.Z_NEAR,
				Camera.Z_FAR);
		entity.addComponent(camera);
		if(current) camera.setAsCurrent();
		return entity;
	}

	/**
	 * Loads an Entity definition XML file into the game.
	 * @param xmlFilePath xml file path.
	 */
	public final void populate(String xmlFilePath) {
		sceneReader.read(xmlFilePath);
	}

	/**
	 * Loads an Entity definition XML file into the game.
	 * @param xmlFilePath xml file path.
	 */
	public final void readEntity(String xmlFilePath, String name) {
		entityReader.read(xmlFilePath, name);
	}

	/**
	 * Loads a value definitions file into the game system.
	 * @param filePath resource file path.
	 */
	public void define(String filePath) {
		
	}

	/**
	 * Finds an Entity on the game. Raises an exception if it is not found.
	 * @param name	entity's unique tag.
	 * @return		the entity if found.
	 */
	public final Entity findEntity(String name) {
		Entity entity = nameRelations.get(name);
		if(entity == null)
			throw new JMokaException("There's no entity with name " + name + ".");
		return entity;
	}

	/**
	 * Called every update frame, is not necessary for most games do.
	 * @param delta
	 */
	public void onUpdate(double delta) { }
	
	/**
	 * Creates the game context, this is intended to start everything the game needs.
	 */
	public abstract void onCreate();
	
	/**
	 * Been capable to send an error code.
	 */
	public abstract void onStop();
}
