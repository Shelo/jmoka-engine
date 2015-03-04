package com.moka.graphics;

import com.moka.components.Sprite;
import com.moka.core.Moka;

public class Drawer {
	private Quad quad;
	
	public Drawer() {
		
	}

	public void draw(Sprite sprite) {
		sprite.getTexture().bind();
		Moka.getRenderer().getShader().update(sprite.getTransform(), sprite);
		quad.draw();
	}
}
