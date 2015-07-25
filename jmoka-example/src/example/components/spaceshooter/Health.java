package example.components.spaceshooter;

import com.moka.core.ComponentAttribute;
import com.moka.core.entity.Component;
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
