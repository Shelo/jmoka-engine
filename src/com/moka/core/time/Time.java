package com.moka.core.time;

import com.moka.core.entity.Component;
import com.moka.core.SubEngine;
import com.moka.triggers.Trigger;

import java.util.ArrayList;

public class Time extends SubEngine
{
	private ArrayList<StopWatch> stopWatches = new ArrayList<>();

	private double elapsed;
	private double delta;

	public void update(double delta)
	{
		this.delta = delta;
		elapsed += delta;

		for(StopWatch stopWatch : stopWatches)
		{
			stopWatch.update(delta);
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

	public <T> Timer<T> newTimer(Component component, float time, Trigger<T> trigger)
	{
		Timer<T> timer = new Timer<>(component, time, trigger);
		stopWatches.add(timer);
		return timer;
	}

	public StopWatch newStopWatch()
	{
		StopWatch stopWatch = new StopWatch();
		stopWatches.add(stopWatch);
		return stopWatch;
	}
}
