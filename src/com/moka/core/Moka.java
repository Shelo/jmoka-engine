package com.moka.core;

import com.moka.graphics.Display;
import com.moka.graphics.Renderer;
import com.moka.physics.Physics;

public final class Moka {
	private static Renderer renderer;
	private static Physics physics;
	private static Display display;
	private static Context game;
	private static Core core;

	public static void init(Context game, float fc) {
		Moka.game = game;

		renderer = new Renderer();
		display = new Display();
		core = new Core(game, fc);
		physics = new Physics();
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

	public static Context getGame() {
		return game;
	}

	public static void start() {
		core.start();
	}

	public static void stop() {
		core.stop();
	}

	public static Physics getPhysics() {
		return physics;
	}
}
