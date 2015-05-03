package example;

import com.moka.core.sync.EntitySchemeWriter;
import com.moka.components.Debugger;
import example.components.*;

import java.util.ArrayList;
import java.util.List;

public class XmlSync
{
    public static void main(String[] args)
    {
        EntitySchemeWriter.register(DirectionalMovement.class);
        EntitySchemeWriter.register(ShipMovement.class);
        EntitySchemeWriter.register(Debugger.class);
        EntitySchemeWriter.register(SpotOn.class);
        EntitySchemeWriter.register(DestroyOnLeave.class);
        EntitySchemeWriter.register(Health.class);
        EntitySchemeWriter.register(EnemyMovementController.class);

        EntitySchemeWriter.render();
    }
}
