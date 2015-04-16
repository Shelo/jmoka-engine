package example.spaceinvaders;

import com.moka.core.Component;

public class DebugPosition extends Component
{
    @Override
    public void onUpdate()
    {
        System.out.println(getTransform().getPosition());
    }
}
