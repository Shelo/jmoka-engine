package com.moka.scene.entity;

import com.moka.core.Moka;
import com.moka.signals.Receiver;
import com.moka.time.Time;
import com.moka.triggers.Trigger;
import com.moka.utils.JMokaException;
import com.moka.utils.JMokaLog;

/**
 * Component class, establishes the common way a component should behave.
 * Class designed for inheritance.
 *
 * @author Shelo
 */
public abstract class Component implements Receiver
{
    private boolean enabled = true;
    private Entity entity;

    public final void setEntity(final Entity entity)
    {
        if (this.entity != null) {
            throw new JMokaException("This component already has an entity.");
        }

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
        if (trigger != null) {
            return trigger.trigger(this, null);
        }

        return null;
    }

    public <T> Object callTrigger(Trigger<T> trigger, T meta)
    {
        if (trigger != null) {
            return trigger.trigger(this, meta);
        }

        return null;
    }

    protected void registerAsReceiverFor(String signal)
    {
        Moka.getChannel().registerReceiver(signal, this);
    }

    protected void emit(String signal, Object value)
    {
        Moka.getChannel().broadcast(signal, value);
    }

    /**
     * Logs a descriptive message.
     */
    public void log(String message)
    {
        String tag = getEntity().getName() + " -> " + this.getClass().getSimpleName();
        JMokaLog.o("Component:" + tag, message);
    }

    /**
     * Throws a descriptive {@link JMokaException}. This will crash the Engine in a graceful
     * way.
     *
     * @param message the message for the exception.
     */
    public void raiseError(String message)
    {
        String tag = getEntity().getName() + "->" + this.getClass().getSimpleName();
        throw new JMokaException("[Component:" + tag + "] " + message);
    }

    /**
     * Logs an error silently (i.e. not crashing).
     *
     * @param message the message for the exception.
     */
    public void silentError(String message)
    {
        String tag = getEntity().getName() + "->" + this.getClass().getSimpleName();
        System.err.println("[Component:" + tag + "] " + message);
    }

    /**
     * Shortcut method to retrieve the delta time (see {@link Time#getDelta()}).
     *
     * @return the delta time.
     */
    public float getDelta()
    {
        return Moka.getTime().getDelta();
    }

    @Override
    public void onSignal(String signal, Object value)
    {

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
