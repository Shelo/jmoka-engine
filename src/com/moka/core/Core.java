package com.moka.core;

import com.moka.exceptions.JMokaException;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Core of the engine, takes care of the game loop and triggers.
 */
public class Core {
	private static final String TAG = "CORE";

	private float frameTime;
	private boolean daemon;
	private Context game;

	/**
	 * Creates a new instance of the engine.
	 * @param fc		frame cap of the cycle.
	 * @param game		a certain game to be run.
	 */
	public Core(Context game, float fc) {
		this.game 	= game;

		frameTime = 1 / fc;
		if (glfwInit() == 0)
			throw new JMokaException("Error initializing GLFW.");
		else
			JMokaLog.o(TAG, "GLFW initialized.");
	}

	public void start() {
		if (daemon) return;
		daemon = true;
		game.onCreate();
		game.createAll();
		run();
	}

	public void run() {
		// fixed delta time.
		double delta = frameTime;

		// current time var and accumulator.
		double currentTime = glfwGetTime();
		double accumulator = 0.0;

		// this is just to show some statics.
		int updateFrames = 0;
		int renderFrames = 0;
		double accSeconds = 0;

		while (daemon) {
			// indicates if we should render or not.
			boolean render = false;

			// get the time that the render frame lasted.
			double newTime = glfwGetTime();
			double frameTime = newTime - currentTime;
			currentTime = newTime;

			// increase the accumulator by this frame time.
			accumulator += frameTime;

			// "spend" the accumulator time until is lower than the delta time.
			// while we have time accumulated left, we onUpdate the scene.
			while (accumulator > delta) {
				// we'll need to render only if we updated the scene... obviously.
				render = true;

				Time.update(delta);
				game.updateAll();
				game.onUpdate();
				Moka.getPhysics().checkCollisions(game);
				game.postUpdate();
				game.clean();
				Input.update();

				// check if user closed the window.
				if (Moka.getDisplay().isCloseRequested())
					daemon = false;

				// subtract used delta time and add it to the game time.
				accumulator -= delta;
				accSeconds 	+= delta;

				updateFrames++;
			}

			// when the accumulator is over, render and onUpdateAll the display.
			if (render) {
				renderFrames++;
				Moka.getRenderer().render(game);
				Moka.getDisplay().onUpdate();
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.err.println("Cannot use optimization.");
				}
			}

			if(accSeconds >= 1) {
				JMokaLog.o(TAG, renderFrames + " fps, " + updateFrames + " ups.");
				accSeconds = renderFrames = updateFrames = 0;
			}
		}

		stop();
	}

	public void stop() {
		JMokaLog.o(TAG, "Stopping JMoka Engine.");
		daemon = false;
		glfwTerminate();
	}
}
