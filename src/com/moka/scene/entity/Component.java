package com.moka.scene.entity;

import com.moka.core.Moka;
import com.moka.triggers.Trigger;
import com.moka.utils.JMokaException;
import com.moka.utils.JMokaLog;

/**
 * Component class, establishes the common way a component should behave.
 * Class designed for inheritance.
 *
 * @author Shelo
 */
public abstract class Component
{
    private boolean enabled = true;
    private Entity entity;

    public final void setEntity(final Entity entity)
    {
        if (this.entity != null)
            throw new JMokaException("This component already has an entity.");

        this.entity = entity;
    }

    public final Entity getEntity()
    {
        return entity;
    }

    public final Entity findEntity(String tag)
    {
        return entity.getScene().findEntity(tag);
    }

    public Transform getTransform()
    {
        return entity.getTransform();
    }

    public final <T extends Component> T getComponent(Class<T> componentClass)
    {
        return entity.getComponent(componentClass);
    }

    @ComponentAttribute("Enabled")
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public Object callTrigger(Trigger<Object> trigger)
    {
        if (trigger != null)
            return trigger.trigger(this, null);

        return null;
    }

    public <T> Object callTrigger(Trigger<T> trigger, T meta)
    {
        if (trigger != null)
            return trigger.trigger(this, meta);

        return null;
    }

    /**
     * Logs a descriptive message.
     */
    public void log(String message)
    {
        String tag = getEntity().getName() + " -> " + this.getClass().getSimpleName();
        JMokaLog.o(tag, message);
    }

    /**
     * Throws a descriptive {@link JMokaException}.
     * @param message   the message for the exception.
     */
    public void raise(String message)
    {
        String tag = getEntity().getName() + " -> " + this.getClass().getSimpleName();
        throw new JMokaException("[" + tag + "] " + message);
    }

    public float getDelta()
    {
        return Moka.getTime().getDelta();
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

    public void onFixedUpdate()
    {

    }

    /**
     * Called just before getEntity is removed from the world, this will
     * only be called on enabled components.
     */
    public void onDestroy()
    {

    }

    /**
     * This will be called when the game actually needs to be disposed.
     */
    public void onDispose()
    {

    }
}
