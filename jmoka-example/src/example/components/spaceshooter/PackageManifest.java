package example.components.spaceshooter;

import com.moka.components.Bullet;
import com.moka.core.Package;
import com.moka.core.entity.Component;

import java.util.LinkedList;

public class PackageManifest extends Package
{
    @Override
    public void registerComponents(LinkedList<Class<? extends Component>> components)
    {
        components.add(Movement.class);
        components.add(SimpleShooter.class);
        components.add(Health.class);
    }

    @Override
    public String getCommonName()
    {
        return "SpaceShooter";
    }
}
