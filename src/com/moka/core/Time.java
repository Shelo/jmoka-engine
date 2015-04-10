package com.moka.core;

import java.util.ArrayList;

public class Time extends SubEngine
{
	private ArrayList<Timer> timers = new ArrayList<>();

	private double elapsed;
	private double delta;

	void update(double delta)
	{
		this.delta = delta;
		elapsed += delta;

		for(Timer timer : timers)
		{
			timer.update(delta);
		}
	}

	public double getDelta()
	{
		return delta;
	}

	public double getElapsed()
	{
		return elapsed;
	}

	public Timer newTimer()
	{
		Timer timer = new Timer();
		timers.add(timer);
		return timer;
	}
}
