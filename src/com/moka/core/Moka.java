package com.moka.core;

import com.moka.core.game.BaseGame;
import com.moka.graphics.Display;
import com.moka.graphics.Renderer;

public final class Moka {
	private static Renderer renderer;
	private static Display display;
	private static BaseGame game;
	private static Core core;

	public static void init(BaseGame game, float fc) {
		Moka.game	= game;

		renderer 	= new Renderer();
		display 	= new Display();
		core 		= new Core(game, fc);
	}

	public static Display getDisplay() {
		return display;
	}

	public static Core getCore() {
		return core;
	}

	public static Renderer getRenderer() {
		return renderer;
	}

	public static BaseGame getGame() {
		return game;
	}

	public static double getDelta() {
		return core.getDelta();
	}

	public static void start() {
		core.start();
	}

	public static void stop() {
		core.stop();
	}
}
