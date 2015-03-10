package com.moka.core;

import com.moka.exceptions.JMokaException;
import com.moka.math.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Input manager of the engine, this manager is intended to be used as a static method class.
 * @author Shelo
 */
public final class Input {
	private static final int MOUSE_COUNT 	= 5;
	private static final int KEY_COUNT		= 348;

	private static Vector2f cursorPosition;
	private static DoubleBuffer cursorPosX;
	private static DoubleBuffer cursorPosY;
	private static boolean[] activeMouse;
	private static boolean[] activeKeys;
	private static long window;

	public static void onCreate(long window) {
		Input.window = window;

		activeMouse = new boolean[MOUSE_COUNT];
		activeKeys	= new boolean[KEY_COUNT];

		cursorPosX = BufferUtils.createDoubleBuffer(1);
		cursorPosY = BufferUtils.createDoubleBuffer(1);

		cursorPosition = new Vector2f();
	}

	public static void update() {
		for(int i = 0; i < KEY_COUNT; i++)
			activeKeys[i] = getKey(i);

		for(int i = 0; i < MOUSE_COUNT; i++)
			activeMouse[i] = getMouse(i);

		cursorPosX.rewind();
		cursorPosY.rewind();
		glfwGetCursorPos(window, cursorPosX, cursorPosY);
		
		
	}
	
	public static boolean getKey(int keyCode) {
		validate();
		return glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS;
	}
	
	public static boolean getKeyDown(int keyCode) {
		validate();
		return getKey(keyCode) && !activeKeys[keyCode];
	}

	public static boolean getKeyUp(int keyCode) {
		validate();
		return !getKey(keyCode) && activeKeys[keyCode];
	}
	
	public static boolean getMouse(int button) {
		validate();
		return glfwGetMouseButton(window, button) == GLFW.GLFW_PRESS;
	}

	public static boolean getMouseDown(int buttonCode) {
		validate();
		return getMouse(buttonCode) && !activeMouse[buttonCode];
	}

	public static boolean getMouseUp(int buttonCode) {
		validate();
		return !getMouse(buttonCode) && activeMouse[buttonCode];
	}

	public static int getCursorPosX() {
		int x = (int) cursorPosX.get();
		cursorPosX.rewind();
		return x;
	}

	public static int getCursorPosY() {
		int y = (int) cursorPosY.get();
		cursorPosY.rewind();
		return Moka.getDisplay().getHeight() - y;
	}

	private static void validate() {
		if(window == 0)
			throw new JMokaException("Window not defined in input.");
	}

	public static Vector2f getCursorPos() {
		return cursorPosition.set(getCursorPosX(), getCursorPosY());
	}
}
