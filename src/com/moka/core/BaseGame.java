package com.moka.core;

import com.moka.components.Camera;
import com.moka.core.xml.XmlEntityReader;
import com.moka.core.xml.XmlPrefabReader;
import com.moka.core.xml.XmlSceneReader;
import com.moka.exceptions.JMokaException;
import com.moka.graphics.Shader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Base class of a game, takes care of internal core usage.
 */
public abstract class BaseGame {
	private HashMap<String, Entity> nameRelations;
	private XmlEntityReader entityReader;
	private ArrayList<Entity> entities;
	private XmlSceneReader sceneReader;
	private XmlPrefabReader prefabReader;

	public BaseGame() {
		nameRelations = new HashMap<>();
		entities = new ArrayList<>();
		sceneReader = new XmlSceneReader(this);
		entityReader = sceneReader.getEntityReader();
		prefabReader = new XmlPrefabReader(entityReader, this);
	}

	public final void updateAll() {
		for (Entity entity : entities)
			entity.update();
	}

	public final void createAll() {
		for (Entity entity : entities)
			entity.create();
	}

	public final void renderAll(Shader shader) {
		for (Entity entity : entities)
			if (entity.hasSprite() && entity.getSprite().isEnabled())
				entity.getSprite().render(shader);
	}

	public void postUpdate() {
		for(Entity entity : entities)
			entity.postUpdate();
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
	 * Creates a new prefab using the XmlPrefabReader defined in this context.
	 * @param filePath the path for the XML Entity file.
	 * @return the prefab.
	 */
	public Prefab newPrefab(String filePath) {
		return prefabReader.newPrefab(filePath);
	}

	/**
	 * Loads an Entity definition XML file into the game.
	 * @param xmlFilePath xml file path.
	 * @deprecated use newPrefab to create a prefab object to create new entities.
	 */
	public final void readEntity(String xmlFilePath, String name) {
		entityReader.read(xmlFilePath, name);
	}

	/**
	 * Loads a value definitions file into the game system.
	 * @param filePath resource file path.
	 */
	public void define(String filePath) {
		Resources.loadResources(filePath);
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

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	/**
	 * Called every update frame, is not necessary for most games do.
	 */
	public void onUpdate() { }

	/**
	 * Creates the game context, this is intended to start everything the game needs.
	 */
	public abstract void onCreate();

	/**
	 * Been capable to send an error code.
	 */
	public abstract void onStop();
}
