package example.components;

import com.moka.components.Component;
import com.moka.core.Prefab;
import com.moka.math.Vector2f;

public class Background extends Component
{
    private Prefab grass1;
    private Prefab grass2;

    @Override
    public void onCreate()
    {
        grass1 = newPrefab("res/scene/entities/grass1.xml");
        grass2 = newPrefab("res/scene/entities/grass2.xml");

        int cols = 16;
        int rows = 12;

        for (int i = 0; i < cols; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                if ((i * 31 + j * 11) % 2 == 0)
                {
                    grass1.setPosition(new Vector2f(i * 64 + 32, j * 64 + 32));
                    grass1.newEntity(null);
                }
                else
                {
                    grass2.setPosition(new Vector2f(i * 64 + 32, j * 64 + 32));
                    grass2.newEntity(null);
                }
            }
        }
    }
}
