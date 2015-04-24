package com.moka.core;

import com.moka.graphics.Display;
import com.moka.physics.Collider;
import com.moka.triggers.Trigger;
import com.moka.utils.JMokaException;
import com.moka.utils.JMokaLog;

/**
 * Component class, establishes the common way a component should behave.
 * Class designed for inheritance.
 * @author Shelo
 */
public abstract class Component
{
	private boolean enabled = true;
	private Entity entity;

	public final void setEntity(final Entity entity)
	{
		if (this.entity != null)
		{
			throw new JMokaException("This component already has an entity.");
		}

		this.entity = entity;
	}

	public final Transform getTransform() {
		return entity.getTransform();
	}

	public final Entity entity() {
		return entity;
	}

	public final Entity findEntity(String tag) {
		return getApplication().getContext().findEntity(tag);
	}

	public final <T extends Component> T getComponent(Class<T> componentClass) {
		return entity.getComponent(componentClass);
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public <T> Object callTrigger(Trigger<T> trigger, T meta)
	{
		if (trigger != null)
		{
			return trigger.trigger(this, meta);
		}

		return null;
	}

	/**
	 * Creates a new prefab from a XML file. Remember: The Prefab is a state machine.
	 *
	 * @param filePath	the xml file.
	 * @return the new creaed prefab.
	 */
	public Prefab newPrefab(String filePath) {
		return entity.getContext().newPrefab(filePath);
	}

	public boolean hasCollider() {
		return entity.hasCollider();
	}

	public Collider getCollider() {
		return entity.getCollider();
	}

	public Application getApplication()
	{
		return entity.getContext().getApplication();
	}

	/**
	 * [Shortcut] Gets the current context's input.
	 * @return the input object.
	 */
	public Input getInput()
	{
		return getApplication().getInput();
	}

	/**
	 * [Shortcut] Gets the current context's display.
	 * @return the display object.
	 */
	public Display getDisplay()
	{
		return getApplication().getDisplay();
	}

	/**
	 * [Shortcut] Gets the current context's time.
	 * @return the time object.
	 */
	public Time getTime()
	{
		return getApplication().getTime();
	}

	/**
	 * [Shortcut] Gets the current context's resources.
	 * @return the resources object.
	 */
	public Resources getResources()
	{
		return getApplication().getResources();
	}

	/**
	 * Logs a descriptive message to the console.
	 */
	public void log(String message)
	{
		String tag = entity().getName() + " -> " + this.getClass().getSimpleName();
		JMokaLog.o(tag, message);
	}

	/**
	 * Called at the creation time.
	 */
	public void onCreate()
	{

	}

	/**
	 * Called every update frame.
	 */
	public void onUpdate()
	{

	}

	/**
	 * Called just after the physics system resolved every collision.
	 */
	public void onPostUpdate()
	{

	}

	/**
	 * Called just before wrapping entity is removed
	 * from entities collection
	 */
	public void onDestroy()
	{

	}
}
