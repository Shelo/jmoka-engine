package com.moka.triggers;

import com.moka.components.Camera;
import com.moka.math.Vector2;
import com.moka.utils.Pools;

public class CursorTriggers
{
    public static class MoveToCursor extends Trigger
    {
        @Override
        public Object onTrigger()
        {
            Vector2 buffer = Pools.vector2.take();

            Camera camera = getApplication().getRenderer().getCamera();
            getTransform().setPosition(camera.moveToWorldCoords(getComponent().getInput().getCursorPos(), buffer));

            Pools.vector2.put(buffer);
            return true;
        }
    };
}
