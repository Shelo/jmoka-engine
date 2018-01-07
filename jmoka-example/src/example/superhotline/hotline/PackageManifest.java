package example.superhotline.hotline;

import com.moka.core.Package;
import com.moka.scene.entity.Component;

import java.util.LinkedList;

public class PackageManifest extends Package
{
    @Override
    public void registerComponents(LinkedList<Class<? extends Component>> components)
    {
        components.add(MoveTowards.class);

    }

    @Override
    public String getCommonName()
    {
        return "Hotline";
    }
}
