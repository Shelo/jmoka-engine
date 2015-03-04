package com.moka.test;

import com.moka.core.Entity;
import com.moka.core.Moka;
import com.moka.core.game.XmlGame;
import com.moka.math.Vector2;

/**
 * Main entrance of the engine. Write initialization here.
 */
public class Game {
	public static void main(String[] args) {
		Entity entity = new Entity("Name");
		entity.getTransform().setRotation(90);
		entity.getTransform().setPosition(10, 10);
		entity.getTransform().setSize(10, 10);

		Vector2[] res = entity.transformVertices(new Vector2[] {
				new Vector2(0.5f, 0.5f)
		});

		for(Vector2 re : res) {
			System.out.println(re);
		}

		// initialize the engine giving it a particular game.
		Moka.init(new XmlGame("res/scene/scene.xml"), 100);

		// create the display.
		Moka.getDisplay().createDisplay(360, 480, "JMoka Engine");

		// start the engine cycle.
		Moka.start();
    }
}
