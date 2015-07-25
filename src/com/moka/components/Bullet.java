package com.moka.components;

import com.moka.core.Moka;
import com.moka.core.entity.Component;
import com.moka.core.ComponentAttribute;
import com.moka.math.Vector2;
import com.moka.utils.Pools;

public class Bullet extends Component
{
    public static final byte NONE = 0x0;
    public static final byte LIFE_TIME = 0x1;
    public static final byte MAX_DISTANCE = 0x2;
    public static final byte LIFE_TIME_AND_MAX_DISTANCE = 0x3;

    private Vector2 origin;
	private float speed = 300;
    private float lifeTime;
    private float maxDistance;
    private byte destroyCondition = NONE;

	public Bullet()
	{

	}

	@Override
	public void onCreate()
	{
        if ((destroyCondition & MAX_DISTANCE) != 0)
        {
            origin = getTransform().getPosition().cpy();
        }
	}

	@Override
	public void onUpdate()
	{
		Vector2 buffer = Pools.vec2.take(0, 0);

		buffer.set(getTransform().getFront(buffer)).mul(speed * Moka.getTime().getDelta());
		getTransform().move(buffer);

        if ((destroyCondition & LIFE_TIME) != 0)
        {
            checkLifeTime();
        }
        else if ((destroyCondition & MAX_DISTANCE) != 0)
        {
            checkMaxDistance();
        }

		Pools.vec2.put(buffer);
	}

    private void checkMaxDistance()
    {
        Vector2 position = Pools.vec2.take(getTransform().getPosition());
        float distanceSqr = position.sub(origin).sqrLen();
        Pools.vec2.put(position);

        if (distanceSqr > maxDistance * maxDistance)
        {
            getEntity().destroy();
        }
    }

    private void checkLifeTime()
    {
        lifeTime -= Moka.getTime().getDelta();

        if (lifeTime <= 0)
        {
            getEntity().destroy();
        }
    }

    @Override
    public void onDestroy()
    {
        if (origin != null)
        {
            Pools.vec2.put(origin);
        }
    }

    @ComponentAttribute("Speed")
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

    @ComponentAttribute("LifeTime")
    public void setLifeTime(float lifeTime)
    {
        destroyCondition |= LIFE_TIME;
        this.lifeTime = lifeTime;
    }

    @ComponentAttribute("MaxDistance")
    public void setMaxDistance(float maxDistance)
    {
        destroyCondition |= MAX_DISTANCE;
        this.maxDistance = maxDistance;
    }
}
