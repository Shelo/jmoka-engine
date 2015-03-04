package com.moka.graphics;

import com.moka.core.Input;
import com.moka.core.JMokaLog;
import com.moka.core.Moka;
import com.moka.core.Resources;
import com.moka.exceptions.JMokaException;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;

public final class Display {
	private static final String TAG = "DISPLAY";
	
	private String title;
	private long window;
	private int height;
	private int width;

	public void createDisplay(int width, int height, String title) {
		this.height = height;
		this.width 	= width;
		this.title 	= title;

		glfwWindowHint(GLFW_SAMPLES, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

		// center window.
		ByteBuffer vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (GLFWvidmode.width(vidMode) - width) / 2, (GLFWvidmode.height(vidMode) - height) / 2);

		if(window == 0)
			JMokaException.raise("Window could not be created.");
		else 
			JMokaLog.o(TAG, "Window created.");

		glfwMakeContextCurrent(window);
		glfwShowWindow(window);

		// this is a critical line!!
		GLContext.createFromCurrent();

		// init other instances.
		Moka.getRenderer().onCreate(width, height);
		Input.onCreate(window);
	}

	public void createDisplay(String widthRes, String heightRes, String title) {
		createDisplay(Resources.getInt(widthRes), Resources.getInt(heightRes), title);
	}
	
	public void onUpdate() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	public boolean isCloseRequested() {
		return glfwWindowShouldClose(window) != 0;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}
}
