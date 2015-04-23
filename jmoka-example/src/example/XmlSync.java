package example;

import com.moka.core.sync.EntitySchemeWriter;
import com.moka.components.Debugger;
import example.components.DestroyOnLeave;
import example.components.DirectionalMovement;
import example.components.ShipMovement;
import example.components.SpotOn;

public class XmlSync
{
    public static void main(String[] args)
    {
        EntitySchemeWriter.register(DirectionalMovement.class);
        EntitySchemeWriter.register(ShipMovement.class);
        EntitySchemeWriter.register(Debugger.class);
        EntitySchemeWriter.register(SpotOn.class);
        EntitySchemeWriter.register(DestroyOnLeave.class);

        EntitySchemeWriter.render();
    }
}
