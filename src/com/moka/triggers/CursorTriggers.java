package com.moka.triggers;

public class CursorTriggers
{
    public static Trigger moveToCursor = new Trigger()
    {
        @Override
        public boolean onTrigger()
        {
            getTransform().setPosition(getComponent().getInput().getCursorPos());
            return true;
        }
    };
}
