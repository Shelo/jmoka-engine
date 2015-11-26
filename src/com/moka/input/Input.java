package com.moka.input;

import com.moka.core.SubEngine;
import com.moka.math.Vector2;
import com.moka.utils.JMokaException;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Input engine. This can map input, from mouse, keys and axes to button names. This gives
 * the facility to make settings for the game and change button maps without having to re work
 * the actual input handler code.
 *
 * @author Shelo
 */
public final class Input extends SubEngine
{
    private static final int MOUSE_COUNT = 5;
    private static final int KEY_COUNT = 348;

    private DoubleBuffer cursorPosX;
    private DoubleBuffer cursorPosY;
    private Vector2 cursorPosition;
    private boolean[] activeMouse;
    private boolean[] activeKeys;

    private HashMap<String, Integer> buttonsMouse = new HashMap<>();
    private HashMap<String, Integer> buttonsKeys = new HashMap<>();
    private HashMap<String, Axes> axes = new HashMap<>();

    public static final int KEY_UNKNOWN = -1;
    public static final int KEY_SPACE = 32;
    public static final int KEY_APOSTROPHE = 39;
    public static final int KEY_COMMA = 44;
    public static final int KEY_MINUS = 45;
    public static final int KEY_PERIOD = 46;
    public static final int KEY_SLASH = 47;
    public static final int KEY_0 = 48;
    public static final int KEY_1 = 49;
    public static final int KEY_2 = 50;
    public static final int KEY_3 = 51;
    public static final int KEY_4 = 52;
    public static final int KEY_5 = 53;
    public static final int KEY_6 = 54;
    public static final int KEY_7 = 55;
    public static final int KEY_8 = 56;
    public static final int KEY_9 = 57;
    public static final int KEY_SEMICOLON = 59;
    public static final int KEY_EQUAL = 61;
    public static final int KEY_A = 65;
    public static final int KEY_B = 66;
    public static final int KEY_C = 67;
    public static final int KEY_D = 68;
    public static final int KEY_E = 69;
    public static final int KEY_F = 70;
    public static final int KEY_G = 71;
    public static final int KEY_H = 72;
    public static final int KEY_I = 73;
    public static final int KEY_J = 74;
    public static final int KEY_K = 75;
    public static final int KEY_L = 76;
    public static final int KEY_M = 77;
    public static final int KEY_N = 78;
    public static final int KEY_O = 79;
    public static final int KEY_P = 80;
    public static final int KEY_Q = 81;
    public static final int KEY_R = 82;
    public static final int KEY_S = 83;
    public static final int KEY_T = 84;
    public static final int KEY_U = 85;
    public static final int KEY_V = 86;
    public static final int KEY_W = 87;
    public static final int KEY_X = 88;
    public static final int KEY_Y = 89;
    public static final int KEY_Z = 90;
    public static final int KEY_LEFT_BRACKET = 91;
    public static final int KEY_BACKSLASH = 92;
    public static final int KEY_RIGHT_BRACKET = 93;
    public static final int KEY_GRAVE_ACCENT = 96;
    public static final int KEY_WORLD_1 = 161;
    public static final int KEY_WORLD_2 = 162;
    public static final int KEY_ESCAPE = 256;
    public static final int KEY_ENTER = 257;
    public static final int KEY_TAB = 258;
    public static final int KEY_BACKSPACE = 259;
    public static final int KEY_INSERT = 260;
    public static final int KEY_DELETE = 261;
    public static final int KEY_RIGHT = 262;
    public static final int KEY_LEFT = 263;
    public static final int KEY_DOWN = 264;
    public static final int KEY_UP = 265;
    public static final int KEY_PAGE_UP = 266;
    public static final int KEY_PAGE_DOWN = 267;
    public static final int KEY_HOME = 268;
    public static final int KEY_END = 269;
    public static final int KEY_CAPS_LOCK = 280;
    public static final int KEY_SCROLL_LOCK = 281;
    public static final int KEY_NUM_LOCK = 282;
    public static final int KEY_PRINT_SCREEN = 283;
    public static final int KEY_PAUSE = 284;
    public static final int KEY_F1 = 290;
    public static final int KEY_F2 = 291;
    public static final int KEY_F3 = 292;
    public static final int KEY_F4 = 293;
    public static final int KEY_F5 = 294;
    public static final int KEY_F6 = 295;
    public static final int KEY_F7 = 296;
    public static final int KEY_F8 = 297;
    public static final int KEY_F9 = 298;
    public static final int KEY_F10 = 299;
    public static final int KEY_F11 = 300;
    public static final int KEY_F12 = 301;
    public static final int KEY_F13 = 302;
    public static final int KEY_F14 = 303;
    public static final int KEY_F15 = 304;
    public static final int KEY_F16 = 305;
    public static final int KEY_F17 = 306;
    public static final int KEY_F18 = 307;
    public static final int KEY_F19 = 308;
    public static final int KEY_F20 = 309;
    public static final int KEY_F21 = 310;
    public static final int KEY_F22 = 311;
    public static final int KEY_F23 = 312;
    public static final int KEY_F24 = 313;
    public static final int KEY_F25 = 314;
    public static final int KEY_KP_0 = 320;
    public static final int KEY_KP_1 = 321;
    public static final int KEY_KP_2 = 322;
    public static final int KEY_KP_3 = 323;
    public static final int KEY_KP_4 = 324;
    public static final int KEY_KP_5 = 325;
    public static final int KEY_KP_6 = 326;
    public static final int KEY_KP_7 = 327;
    public static final int KEY_KP_8 = 328;
    public static final int KEY_KP_9 = 329;
    public static final int KEY_KP_DECIMAL = 330;
    public static final int KEY_KP_DIVIDE = 331;
    public static final int KEY_KP_MULTIPLY = 332;
    public static final int KEY_KP_SUBTRACT = 333;
    public static final int KEY_KP_ADD = 334;
    public static final int KEY_KP_ENTER = 335;
    public static final int KEY_KP_EQUAL = 336;
    public static final int KEY_LEFT_SHIFT = 340;
    public static final int KEY_LEFT_CONTROL = 341;
    public static final int KEY_LEFT_ALT = 342;
    public static final int KEY_LEFT_SUPER = 343;
    public static final int KEY_RIGHT_SHIFT = 344;
    public static final int KEY_RIGHT_CONTROL = 345;
    public static final int KEY_RIGHT_ALT = 346;
    public static final int KEY_RIGHT_SUPER = 347;
    public static final int KEY_MENU = 348;
    public static final int KEY_LAST = 348;
    public static final int MOD_SHIFT = 1;
    public static final int MOD_CONTROL = 2;
    public static final int MOD_ALT = 4;
    public static final int MOD_SUPER = 8;
    public static final int MOUSE_BUTTON_1 = 0;
    public static final int MOUSE_BUTTON_2 = 1;
    public static final int MOUSE_BUTTON_3 = 2;
    public static final int MOUSE_BUTTON_4 = 3;
    public static final int MOUSE_BUTTON_5 = 4;
    public static final int MOUSE_BUTTON_6 = 5;
    public static final int MOUSE_BUTTON_7 = 6;
    public static final int MOUSE_BUTTON_8 = 7;
    public static final int MOUSE_BUTTON_LAST = 7;
    public static final int MOUSE_BUTTON_LEFT = 0;
    public static final int MOUSE_BUTTON_RIGHT = 1;
    public static final int MOUSE_BUTTON_MIDDLE = 2;

    public void create()
    {
        activeMouse = new boolean[MOUSE_COUNT];
        activeKeys = new boolean[KEY_COUNT];

        cursorPosX = BufferUtils.createDoubleBuffer(1);
        cursorPosY = BufferUtils.createDoubleBuffer(1);

        cursorPosition = new Vector2();
    }

    public void update()
    {
        for (int i = 0; i < KEY_COUNT; i++)
            activeKeys[i] = getKey(i);

        for (int i = 0; i < MOUSE_COUNT; i++)
            activeMouse[i] = getMouse(i);

        cursorPosX.rewind();
        cursorPosY.rewind();
        glfwGetCursorPos(getWindow(), cursorPosX, cursorPosY);
        cursorPosition.set(getCursorPosX(), getCursorPosY());
    }

    /**
     * Binds a button name to a key. The button name has no restrictions as you can use
     * whatever string you need.
     *
     * @param button    The button name.
     * @param keyCode   The key code to be bound.
     * @return          This class for chaining.
     */
    public Input bindKey(String button, int keyCode)
    {
        if (button == null)
            throw new JMokaException("Button name cannot be null.");

        if (buttonsMouse.containsKey(button))
            throw new JMokaException("Button is already set for mouse.");

        buttonsKeys.put(button, keyCode);

        return this;
    }

    /**
     * Binds a button name to an axis. The button name has no restrictions as you can use
     * whatever string you need. An axis interpolates between the negative and the positive given the
     * user input. The value of the axis is always between -1 and 1, inclusive.
     *
     * Retrieve the value with {@link Input#getAxes(String)}
     *
     * @param name      The button name.
     * @param negative  The negative key code.
     * @param positive  The positive key code.
     * @return          This class for chaining.
     */
    public Input bindAxes(String name, int negative, int positive)
    {
        axes.put(name, new Axes(negative, positive));
        return this;
    }

    /**
     * Binds a button to a mouse button. The button cannot be previously defined in keyboard
     * buttons as this causes a confusion when retrieving.
     *
     * Retrieve the value with {@link Input#getButton(String)}.
     *
     * @param button    the button name to be bound.
     * @param keyCode   the mouse button.
     * @return          this object for chaining.
     */
    public Input bindMouse(String button, int keyCode)
    {
        if (button == null)
            throw new JMokaException("Button name cannot be null.");

        if (buttonsKeys.containsKey(button))
            throw new JMokaException("Button is already set for keyboard.");

        buttonsMouse.put(button, keyCode);

        return this;
    }

    public int getButtonKeyCode(String button)
    {
        if (!buttonsKeys.containsKey(button))
            throw new JMokaException("Keyboard button " + button + " is not set.");

        return buttonsKeys.get(button);
    }

    public int getButtonMouseCode(String button)
    {
        if (!buttonsMouse.containsKey(button))
            throw new JMokaException("Mouse button " + button + " is not set.");

        return buttonsMouse.get(button);
    }

    public boolean getButton(String button)
    {
        if (buttonsKeys.containsKey(button))
            return getKey(getButtonKeyCode(button));
        else
            return getMouse(getButtonMouseCode(button));
    }

    /**
     * With the given axes, this tells which direction the player is pressing.
     *
     * @param name  the axes name.
     * @return      -1, 0 or 1 depending on the axes.
     */
    public float getAxes(String name)
    {
        int value = 0;

        if (!axes.containsKey(name))
            throw new JMokaException("The axes with name " + name + " does not exists.");

        Axes found = axes.get(name);

        if (getKey(found.getPositive()))
            value += 1;

        if (getKey(found.getNegative()))
            value -= 1;

        return value;
    }

    public boolean getButtonDown(String button)
    {
        if (buttonsKeys.containsKey(button))
            return getKeyDown(getButtonKeyCode(button));
        else
            return getMouseDown(getButtonMouseCode(button));
    }

    public boolean getButtonUp(String button)
    {
        if (buttonsKeys.containsKey(button))
            return getKeyUp(getButtonKeyCode(button));
        else
            return getMouseUp(getButtonMouseCode(button));
    }

    public boolean getKey(int keyCode)
    {
        validate();
        boolean result = glfwGetKey(getWindow(), keyCode) == GLFW.GLFW_PRESS;

        if (result && !getDisplay().hasFocus())
            getDisplay().fixFocus();

        return result;
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
        boolean result = glfwGetMouseButton(getWindow(), button) == GLFW.GLFW_PRESS;

        if (result && !getDisplay().hasFocus())
            getDisplay().fixFocus();

        return result;
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
        if (getWindow() == 0)
            throw new JMokaException("Window not defined in input.");
    }

    private long getWindow()
    {
        return getDisplay().getWindow();
    }

    /**
     * Returns the position vector for the cursor. This vector is
     * updated every frame, so you can store and use it to get the
     * position of the mouse without needing to call this method anymore.
     *
     * @return the mouse's position vector
     */
    public Vector2 getCursorPos()
    {
        return cursorPosition;
    }

    /**
     * Returns the position vector for the cursor related to the world coordinates.
     * This gets the renderer current camera and uses the method
     * {@link com.moka.components.Camera#toWorldCoords(Vector2, Vector2)} to translate.
     *
     * @param buffer    a buffer vector to store the position.
     * @return          the position of the cursor relative to the screen.
     */
    public Vector2 getCursorWorldPos(Vector2 buffer)
    {
        return getRenderer().getCamera().toWorldCoords(getCursorPos(), buffer);
    }

    /**
     * Clears all bindings inside the input, including: mouse, keys and axes.
     */
    public void clear()
    {
        buttonsMouse.clear();
        buttonsKeys.clear();
        axes.clear();
    }
}
