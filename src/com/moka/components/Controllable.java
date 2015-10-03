package com.moka.components;

import com.moka.core.Moka;
import com.moka.math.MathUtil;
import com.moka.math.Vector2;
import com.moka.scene.entity.Component;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.utils.Pools;

public class Controllable extends Component
{
	private float acceleration = 600;
    private float drag = 1000;
	private float topSpeed = 500;
	private String axisHorizontal;
    private String axisVertical;

    private Vector2 currentSpeed = new Vector2();

	@Override
	public void onUpdate()
	{
        float dx = Moka.getInput().getAxes(axisHorizontal);
        float dy = Moka.getInput().getAxes(axisVertical);

        currentSpeed.x -= drag / topSpeed * currentSpeed.x * getDelta();
        currentSpeed.y -= drag / topSpeed * currentSpeed.y * getDelta();

        currentSpeed.x += acceleration * dx * getDelta();
        currentSpeed.y += acceleration * dy * getDelta();

        currentSpeed.clampXY(- topSpeed, topSpeed, - topSpeed, topSpeed);
        getTransform().move(currentSpeed.x * getDelta(), currentSpeed.y * getDelta());
    }

    @ComponentAttribute("TopSpeed")
	public void setTopSpeed(int topSpeed)
	{
		this.topSpeed = topSpeed;
	}

	@ComponentAttribute("Acceleration")
	public void setAcceleration(int acceleration)
	{
		this.acceleration = acceleration;
	}

    @ComponentAttribute(value = "AxisHorizontal", required = true)
    public void setAxisHorizontal(String axisHorizontal)
    {
        this.axisHorizontal = axisHorizontal;
    }

    @ComponentAttribute(value = "AxisVertical", required = true)
    public void setAxisVertical(String axisVertical)
    {
        this.axisVertical = axisVertical;
    }

    @ComponentAttribute("Drag")
    public void setDrag(float drag)
    {
        this.drag = drag;
    }
}
