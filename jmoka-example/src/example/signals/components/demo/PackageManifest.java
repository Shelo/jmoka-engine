package example.signals.components.demo;

import com.moka.core.Package;
import com.moka.scene.entity.Component;

import java.util.LinkedList;

public class PackageManifest extends Package
{
    @Override
    public void registerComponents(LinkedList<Class<? extends Component>> components)
    {
        components.add(DemoEmitter.class);
        components.add(DemoReceiver.class);

    }

    @Override
    public String getCommonName()
    {
        return "Demo";
    }
}
