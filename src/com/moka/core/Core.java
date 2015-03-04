package com.moka.core;

import com.moka.core.game.BaseGame;
import com.moka.exceptions.JMokaException;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

/**
 * Core of the engine, takes care of the game loop and triggers.
 */
public class Core {
	private static final String TAG = "CORE";

	private float frameTime;
	private boolean daemon;
	private BaseGame game;
	private double delta;

	/**
	 * Creates a new instance of the engine.
	 * @param fc		frame cap of the cycle.
	 * @param game		a certain game to be run.
	 */
	public Core(BaseGame game, float fc) {
		this.game 	= game;

		frameTime = 1 / fc;
		if(glfwInit() == 0)
			JMokaException.raise("Error initializing GLFW.");
		else
			JMokaLog.o(TAG, "GLFW initialized.");
	}

	public void start() {
		if(daemon) return;
		daemon = true;
		game.onCreate();
		game.createAll();
		run();
	}

	public void run() {
		// total time accumulated in the game.
		double time = 0.0;

		// fixed delta time.
		delta = frameTime;

		// current time var and accumulator.
		double currentTime = glfwGetTime();
		double accumulator = 0.0;

		// this is just to show some statics.
		int updateFrames = 0;
		int renderFrames = 0;
		double accSeconds = 0;

		while(daemon) {
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
			while(accumulator > delta) {
				// we'll need to render only if we updated the scene... obviously.
				render = true;

				Time.update(delta);
				game.updateAll();
				game.onUpdate();
				Moka.getPhysics().checkCollisions(game);
				game.postUpdate();
				Input.onUpdate();

				// check if user closed the window.
				if(Moka.getDisplay().isCloseRequested())
					daemon = false;

				// subtract used delta time and add it to the game time.
				accumulator -= delta;
				accSeconds 	+= delta;
				time 		+= delta;

				updateFrames++;
			}

			// when the accumulator is over, render and onUpdateAll the display.
			if(render) {
				renderFrames++;
				Moka.getRenderer().render(game);
				Moka.getDisplay().onUpdate();
			} else {
				try {
					Thread.sleep(1);
				} catch(InterruptedException e) {
					
				}
			}

			if(accSeconds >= 1) {
				JMokaLog.o(TAG, renderFrames + " fps, " + updateFrames + " ups.");
				accSeconds = renderFrames = updateFrames = 0;
			}
		}

		stop();
	}

	public double getDelta() {
		return delta;
	}

	public void stop() {
		daemon = false;
		glfwTerminate();
	}
}
