package example.integration;

import com.moka.scene.entity.Entity;
import com.moka.utils.ConfigDataFile;
import com.shelodev.oping2.structure.Branch;

public class EnemyFactory
{
    Branch enemy1;
    Branch enemy2;

    public EnemyFactory(ConfigDataFile enemyConfig)
    {
        enemy1 = enemyConfig.getBranch(null, "Enemy1");
        enemy2 = enemyConfig.getBranch(null, "Enemy2");
    }

    public void spawnHorde()
    {
        for (int i = 32 * 4; i < R.screen.WIDTH - 32 * 4; i += 32 + 16) {
            Entity entity = R.prefabs.enemy.newEntity(null);
            entity.getTransform().setSize(enemy1.getLeaf("Size").getFloat(0),
                    enemy1.getLeaf("Size").getFloat(1));
            entity.getTransform().setPosition(i, 400);
        }
    }
}
