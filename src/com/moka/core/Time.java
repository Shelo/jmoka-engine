package com.moka.core;

import java.util.ArrayList;

public class Time {
	private static ArrayList<Timer> timers = new ArrayList<>();

	private static double elapsed;
	private static double delta;

	static void update(double delta) {
		Time.delta = delta;
		elapsed += delta;

		for(Timer timer : timers)
			timer.update(delta);
	}

	public static double getDelta() {
		return delta;
	}

	public static double getElapsed() {
		return elapsed;
	}

	public static Timer newTimer() {
		Timer timer = new Timer();
		timers.add(timer);
		return timer;
	}
}
