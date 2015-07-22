package example.components;

import com.moka.core.entity.Component;
import example.Resources;
import example.scenes.SecondScene;

public class ExampleSceneLoader extends Component
{
    @Override
    public void onUpdate()
    {
        if (getInput().getButtonDown(Resources.buttons.FIRE_BUTTON))
        {
            getApplication().getContext().loadScene(SecondScene.class, false);
        }
    }
}
