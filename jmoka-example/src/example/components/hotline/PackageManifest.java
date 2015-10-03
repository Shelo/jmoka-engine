package example.components.hotline;

import com.moka.core.Package;
import com.moka.scene.entity.Component;

import java.util.LinkedList;

public class PackageManifest extends Package
{
    @Override
    public void registerComponents(LinkedList<Class<? extends Component>> components)
    {

    }

    @Override
    public String getCommonName()
    {
        return "Hotline";
    }
}
