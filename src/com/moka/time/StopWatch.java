package com.moka.time;

/**
 * Keeps track of time since it was created or since it was restarted.
 * The way that you should create a timer is by calling the newStopWatch factory
 * method on the context's Time.
 *
 * @author shelo
 */
public class StopWatch
{
    private double elapsed;

    /**
     * This constructor is package local on porpoise. That way the user
     * cannot create one that is not really working.
     */
    StopWatch()
    {

    }

    /**
     * Updates the timer whit a given delta time.
     * @param delta the time that passed since the last update.
     */
    void update(double delta)
    {
        elapsed += delta;
    }

    /**
     * Get the time that this timer has accumulated.
     * @return the elapsed time.
     */
    public double getElapsed()
    {
        return elapsed;
    }

    /**
     * Sets the elapsed time to 0.
     */
    public void restart()
    {
        elapsed = 0;
    }

    /**
     * Checks whenever the time elapsed is greater than a given time.
     *
     * @param time the time to compare.
     * @return true if the elapsed time is greater.
     */
    public boolean isGreaterThan(double time)
    {
        return elapsed > time;
    }

    /**
     * Checks whenever the time elapsed is greater than a given timer's elapsed time.
     *
     * @param other the timer to compare.
     * @return true if the elapsed time is greater.
     */
    public boolean isGreaterThan(StopWatch other)
    {
        return elapsed > other.elapsed;
    }
}
