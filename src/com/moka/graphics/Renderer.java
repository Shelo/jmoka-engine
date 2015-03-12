package com.moka.graphics;

import com.moka.components.Camera;
import com.moka.core.Context;
import com.moka.exceptions.JMokaException;
import com.sun.istack.internal.NotNull;

import static org.lwjgl.opengl.GL11.*;

public final class Renderer {
	public static final String SHADERS_PATH = "res/shaders/";
	
	private Camera camera;
	private Shader shader;

	public void onCreate(int width, int height) {
		// create shader.
		shader = new Shader(SHADERS_PATH + "pass_through_vertex.glsl",
				SHADERS_PATH + "pass_through_fragment.glsl");

		// initialize OpenGL stuff.
		glClearColor(0, 0, 0, 1);

		glFrontFace(GL_CW);
		
		// enable culling for better performance.
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		// enable blending.
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public void render(Context game) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		shader.bind();

		if(camera == null)
			throw new JMokaException("There's no camera attached to the renderer.");

		shader.setUniform("u_projectedView", camera.getProjectedViewMatrix());

		game.renderAll(shader);
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(@NotNull Camera camera) {
		if(camera == null)
			throw new JMokaException("Renderer.setCurrentCamera: the given camera cannot be null.");
		this.camera = camera;
	}

	public Shader getShader() {
		return shader;
	}
}
