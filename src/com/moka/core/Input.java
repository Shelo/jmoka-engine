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
public final class Input extends SubEngine
{
	private static final int MOUSE_COUNT 	= 5;
	private static final int KEY_COUNT		= 348;

	private Vector2f cursorPosition;
	private DoubleBuffer cursorPosX;
	private DoubleBuffer cursorPosY;
	private boolean[] activeMouse;
	private boolean[] activeKeys;

	public void create()
	{
		activeMouse = new boolean[MOUSE_COUNT];
		activeKeys	= new boolean[KEY_COUNT];

		cursorPosX = BufferUtils.createDoubleBuffer(1);
		cursorPosY = BufferUtils.createDoubleBuffer(1);

		cursorPosition = new Vector2f();
	}

	public void update()
	{
		for(int i = 0; i < KEY_COUNT; i++)
		{
			activeKeys[i] = getKey(i);
		}

		for(int i = 0; i < MOUSE_COUNT; i++)
		{
			activeMouse[i] = getMouse(i);
		}

		cursorPosX.rewind();
		cursorPosY.rewind();
		glfwGetCursorPos(getWindow(), cursorPosX, cursorPosY);
	}
	
	public boolean getKey(int keyCode)
	{
		validate();
		return glfwGetKey(getWindow(), keyCode) == GLFW.GLFW_PRESS;
	}
	
	public boolean getKeyDown(int keyCode)
	{
		validate();
		return getKey(keyCode) && !activeKeys[keyCode];
	}

	public boolean getKeyUp(int keyCode)
	{
		validate();
		return !getKey(keyCode) && activeKeys[keyCode];
	}
	
	public boolean getMouse(int button)
	{
		validate();
		return glfwGetMouseButton(getWindow(), button) == GLFW.GLFW_PRESS;
	}

	public boolean getMouseDown(int buttonCode)
	{
		validate();
		return getMouse(buttonCode) && !activeMouse[buttonCode];
	}

	public boolean getMouseUp(int buttonCode)
	{
		validate();
		return !getMouse(buttonCode) && activeMouse[buttonCode];
	}

	public int getCursorPosX()
	{
		int x = (int) cursorPosX.get();
		cursorPosX.rewind();
		return x;
	}

	public int getCursorPosY()
	{
		int y = (int) cursorPosY.get();
		cursorPosY.rewind();
		return getDisplay().getHeight() - y;
	}

	private void validate()
	{
		if(getWindow() == 0)
		{
			throw new JMokaException("Window not defined in input.");
		}
	}

	private long getWindow()
	{
		return getDisplay().getWindow();
	}

	public Vector2f getCursorPos()
	{
		return cursorPosition.set(getCursorPosX(), getCursorPosY());
	}
}
