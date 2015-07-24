package example.components.nonsense;

import com.moka.core.Moka;
import com.moka.core.entity.Component;
import example.Res;
import example.scenes.SecondScene;

public class ExampleSceneLoader extends Component
{
    @Override
    public void onUpdate()
    {
        if (Moka.getInput().getButtonDown(Res.buttons.FIRE_1))
        {
            Moka.getContext().loadScene(SecondScene.class, false);
        }
    }
}
