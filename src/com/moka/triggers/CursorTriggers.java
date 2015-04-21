package com.moka.triggers;

import com.moka.components.Camera;
import com.moka.math.Vector2;
import com.moka.utils.pools.Vector2Pool;

public class CursorTriggers
{
    public static Trigger moveToCursor = new Trigger()
    {
        @Override
        public Object onTrigger()
        {
            Vector2 buffer = Vector2Pool.take();

            Camera camera = getApplication().getRenderer().getCamera();
            getTransform().setPosition(camera.moveToWorldCoords(getComponent().getInput().getCursorPos(), buffer));

            Vector2Pool.put(buffer);
            return true;
        }
    };
}
