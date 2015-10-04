package com.moka.triggers;

import com.moka.components.Camera;
import com.moka.core.Moka;
import com.moka.math.Vector2;
import com.moka.utils.Pools;

public class CursorTriggers
{
    public static class MoveToCursor extends Trigger
    {
        @Override
        public Object onTrigger()
        {
            Vector2 buffer = Pools.vec2.take();

            Camera camera = Moka.getRenderer().getCamera();
            transform().setPosition(camera.toWorldCoords(Moka.getInput().getCursorPos(), buffer));

            Pools.vec2.put(buffer);
            return true;
        }
    }
}
