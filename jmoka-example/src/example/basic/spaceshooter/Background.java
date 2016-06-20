package example.basic.spaceshooter;

import com.moka.prefabs.Prefab;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.scene.entity.Transform;

public class Background extends Component
{
    private Transform space1;
    private Transform space2;
    private Transform player;

    @Override
    public void onUpdate()
    {

    }

    @ComponentAttribute(value = "Space", required = true)
    public void setSpace(Prefab space)
    {
        this.space1 = space.newEntity("Space1").getTransform();
        this.space2 = space.newEntity("Space2").getTransform();
    }

    @ComponentAttribute("Player")
    public void setPlayer(Transform player)
    {
        this.player = player;
    }
}
