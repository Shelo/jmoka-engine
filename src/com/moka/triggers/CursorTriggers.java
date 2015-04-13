package com.moka.triggers;

import com.moka.core.Timer;
import com.moka.core.triggers.Trigger;

public class CursorTriggers
{
    public static Trigger<Timer> moveToCursor = new Trigger<Timer>()
    {
        @Override
        public boolean onTrigger()
        {
            getTransform().setPosition(getComponent().getInput().getCursorPos());
            return false;
        }
    };
}
