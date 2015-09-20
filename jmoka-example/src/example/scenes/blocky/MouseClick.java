package example.scenes.blocky;

import com.moka.core.Moka;
import com.moka.core.entity.Component;
import com.moka.core.entity.Entity;
import com.moka.input.Input;

public class MouseClick extends Component
{
    Entity currentEntity;

    @Override
    public void onUpdate()
    {
        if (Moka.getInput().getMouseDown(Input.MOUSE_BUTTON_1))
        {
            if (currentEntity != null)
            {
                currentEntity.destroy();
            }
        }
    }

    public void setCurrentEntity(Entity currentEntity)
    {
        System.out.println(currentEntity);
        this.currentEntity = currentEntity;
    }
}
