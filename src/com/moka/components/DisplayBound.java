package com.moka.components;

import com.moka.math.Vector2f;

public class DisplayBound extends Component
{
    @Override
    public void onUpdate()
    {
        Vector2f pos = getTransform().getPosition();
        Vector2f size = getTransform().getSize();

        if (pos.x < 0 - size.x / 2 || pos.x > getDisplay().getWidth() + size.x / 2 ||
                pos.y < 0 - size.y / 2 || pos.y > getDisplay().getHeight() + size.y / 2)
        {
            getEntity().destroy();
        }
    }
}
