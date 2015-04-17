package com.moka.triggers;

import java.util.Objects;

public class CursorTriggers
{
    public static Trigger moveToCursor = new Trigger()
    {
        @Override
        public Object onTrigger()
        {
            getTransform().setPosition(getComponent().getInput().getCursorPos());
            return true;
        }
    };
}
