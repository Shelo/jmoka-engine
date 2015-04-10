package example;

import com.moka.core.triggers.Trigger;
import com.moka.core.triggers.TriggerEvent;
import com.moka.math.Vector2f;

/**
 * Wraps all shooter triggers.
 */
public class ShooterTriggers
{
    /**
     * Define a custom trigger for the Shooter Component.
     */
    public static final Trigger<Vector2f> fireTrigger = new Trigger<Vector2f>()
    {
        @Override
        public boolean onTrigger(TriggerEvent<Vector2f> event)
        {
            System.out.println("The bullet direction is: " + event.getMeta());
            return true;
        }
    };
}
