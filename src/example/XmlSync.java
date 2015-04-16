package example;

import com.moka.core.xml.XmlEntitySchemeWriter;
import example.spaceinvaders.DebugPosition;
import example.spaceinvaders.DirectionalMovement;
import example.spaceinvaders.ShipMovement;

public class XmlSync
{
    public static void main(String[] args)
    {
        XmlEntitySchemeWriter.register(DirectionalMovement.class);
        XmlEntitySchemeWriter.register(ShipMovement.class);
        XmlEntitySchemeWriter.register(DebugPosition.class);

        XmlEntitySchemeWriter.render();
    }
}
