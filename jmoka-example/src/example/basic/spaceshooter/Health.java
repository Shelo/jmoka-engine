package example.basic.spaceshooter;

import com.moka.scene.entity.ComponentAttribute;
import com.moka.scene.entity.Component;
import com.moka.triggers.Trigger;

public class Health extends Component
{
    private float healthPoints = 100;
    private float armor = 0;
    private Trigger<Object> onDeath;

    public void takeDamage(float damage)
    {
        healthPoints -= damage * (1 - armor);
        if (healthPoints <= 0)
        {
            if (onDeath != null)
            {
                callTrigger(onDeath);
            }
            else
            {
                getEntity().destroy();
            }
        }
    }

    @ComponentAttribute("OnDeath")
    public void setOnDeath(Trigger<Object> onDeath)
    {
        this.onDeath = onDeath;
    }

    @ComponentAttribute("HealthPoints")
    public void setHealthPoints(float healthPoints)
    {
        this.healthPoints = healthPoints;
    }

    @ComponentAttribute("Armor")
    public void setArmor(float armor)
    {
        this.armor = armor;
    }
}
