package com.moka.core;

public class Timer {
	private double elapsed;
	
	public Timer() {
		
	}

	public void update(double delta) {
		elapsed += delta;
	}

	public double getElapsed() {
		return elapsed;
	}

	public void reset() {
		elapsed = 0;
	}
}
