package example;

import com.moka.core.Application;
import com.moka.core.game.XmlGame;

public class Game
{
    public static void main(String[] args)
    {
        Application application = new Application();

        application.setContext(new XmlGame("res/scene/scene.xml", "res/values.xml"));
        application.getDisplay().createDisplay("screen_width", "screen_height", "JMoka Engine");

        application.create().start(60);
    }
}
