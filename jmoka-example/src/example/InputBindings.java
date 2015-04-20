package example;

import com.moka.core.Input;

public class InputBindings
{
    public static void bindWasd(Input input)
    {
        input.bindKey("left", Input.KEY_A);
        input.bindKey("right", Input.KEY_D);
        input.bindKey("up", Input.KEY_W);
        input.bindKey("down", Input.KEY_S);
    }
}
