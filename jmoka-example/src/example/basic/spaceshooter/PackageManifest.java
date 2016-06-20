package example.basic.spaceshooter;

import com.moka.core.Package;
import com.moka.scene.entity.Component;

import java.util.LinkedList;

public class PackageManifest extends Package
{
    @Override
    public void registerComponents(LinkedList<Class<? extends Component>> components)
    {
        components.add(Background.class);
        components.add(CameraFollow.class);
        components.add(Explosion.class);
        components.add(Health.class);
        components.add(Movement.class);
        components.add(SimpleShooter.class);

    }

    @Override
    public String getCommonName()
    {
        return "SpaceShooter";
    }
}
