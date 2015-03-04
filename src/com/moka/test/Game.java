package com.moka.test;

import com.moka.core.Moka;
import com.moka.core.game.XmlGame;

/**
 * Main entrance of the engine. Write initialization here.
 */
public class Game {
	public static void main(String[] args) {
		// initialize the engine giving it a particular game.
		Moka.init(new XmlGame("res/scene/scene.xml", "res/values.xml"), 100);

		// create the display.
		Moka.getDisplay().createDisplay("screen_width", "screen_height", "JMoka Engine");

		// start the engine cycle.
		Moka.start();
    }
}
