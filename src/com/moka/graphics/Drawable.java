package com.moka.graphics;

import com.moka.scene.entity.Component;

public abstract class Drawable extends Component
{
    public abstract void render(Renderer renderer);
}
