package example.components;

import com.moka.core.entity.Component;
import com.moka.core.readers.ComponentAttribute;
import com.moka.math.MathUtil;
import com.moka.triggers.Trigger;
import com.moka.utils.None;

public class Health extends Component
{
    private float defense = 0;
    private float hitPoints = 100;
    private Trigger<None> destroyTrigger;
    private Trigger<Float> damageTrigger;

    @Override
    public void onUpdate()
    {
        if (hitPoints <= 0)
        {
            Boolean shouldDestroy = (Boolean) callTrigger(destroyTrigger, new None());

            if (shouldDestroy == null || shouldDestroy)
            {
                entity().destroy();
            }
        }
    }

    public void takeDamage(float damage)
    {
        damage *= (1 - defense);
        hitPoints -= damage;
        callTrigger(damageTrigger, damage);
    }

    @ComponentAttribute("defense")
    public void setDefense(float defense)
    {
        this.defense = MathUtil.clamp(defense, 0, 1);
    }

    @ComponentAttribute("damageTrigger")
    public void setDamageTrigger(Trigger<Float> damageTrigger)
    {
        this.damageTrigger = damageTrigger;
    }

    @ComponentAttribute("destroyTrigger")
    public void setDestroyTrigger(Trigger<None> destroyTrigger)
    {
        this.destroyTrigger = destroyTrigger;
    }

    @ComponentAttribute("hitPoints")
    public void setHitPoints(float hitPoints)
    {
        this.hitPoints = hitPoints;
    }
}
