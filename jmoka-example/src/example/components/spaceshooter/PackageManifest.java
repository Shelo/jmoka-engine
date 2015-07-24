package example.components.spaceshooter;

import com.moka.core.Package;
import com.moka.core.entity.Component;
import example.components.nonsense.ExampleSceneLoader;

import java.util.LinkedList;

public class PackageManifest extends Package
{
    @Override
    public String getCommonName()
    {
        return "SpaceShooter";
    }

    @Override
    public void registerComponents(LinkedList<Class<? extends Component>> components)
    {
        components.add(Movement.class);
    }
}
