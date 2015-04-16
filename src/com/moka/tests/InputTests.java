package com.moka.tests;

import com.moka.core.Input;
import com.moka.utils.JMokaException;
import org.junit.Test;
import org.lwjgl.glfw.GLFW;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class InputTests
{
    @Test
    public void addButtons()
    {
        Input input = new Input();
        input.bindKey("up", GLFW.GLFW_KEY_W);
        int code = input.getButtonKeyCode("up");
        assertThat(code, is(equalTo(GLFW.GLFW_KEY_W)));
    }

    @Test(expected = JMokaException.class)
    public void failTwoButtons()
    {
        Input input = new Input();
        input.bindKey("up", GLFW.GLFW_KEY_W);
        input.bindMouse("up", GLFW.GLFW_MOUSE_BUTTON_1);
    }

    @Test(expected = JMokaException.class)
    public void failNoButton()
    {
        Input input = new Input();
        input.getButton("button");
    }
}
