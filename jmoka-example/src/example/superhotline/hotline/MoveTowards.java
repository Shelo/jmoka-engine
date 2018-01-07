package example.superhotline.hotline;

import com.moka.math.Vector2;
import com.moka.resources.utils.EntityBuffer;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.utils.Pools;

public class MoveTowards extends Component {
    private EntityBuffer target;

    @Override
    public void onUpdate() {
        Vector2 distance = Pools.vec2.take();
        distance.set(target.get().getTransform().getPosition());
        distance.sub(getTransform().getPosition());
        distance.nor();
        distance.mul(10);

        System.out.println(getTransform().getPosition());
        getTransform().moveDelta(0.5f, 0.5f);
    }

    @ComponentAttribute("Target")
    public void setTarget(EntityBuffer target) {
        this.target = target;
    }
}
