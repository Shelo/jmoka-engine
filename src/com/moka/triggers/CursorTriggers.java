package com.moka.triggers;

import com.moka.core.Timer;
import com.moka.core.triggers.Trigger;
import com.moka.core.triggers.TriggerEvent;

public class CursorTriggers
{
    public static Trigger<Timer> moveToCursor = new Trigger<Timer>()
    {
        @Override
        public boolean onTrigger(TriggerEvent<Timer> e)
        {
            e.getTransform().setPosition(e.getComponent().getInput().getCursorPos());
            return false;
        }
    };
}
