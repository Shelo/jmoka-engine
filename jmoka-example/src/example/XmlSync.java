package example;

import com.moka.core.sync.EntitySchemeWriter;
import example.components.Debugger;
import example.components.DirectionalMovement;
import example.components.ShipMovement;

public class XmlSync
{
    public static void main(String[] args)
    {
        EntitySchemeWriter.register(DirectionalMovement.class);
        EntitySchemeWriter.register(ShipMovement.class);
        EntitySchemeWriter.register(Debugger.class);

        EntitySchemeWriter.render();
    }
}
