package com.moka.input;

public class Axes
{
    private int positive;
    private int negative;

    public Axes(int negative, int positive)
    {
        this.negative = negative;
        this.positive = positive;
    }

    public void setNegative(int negative)
    {
        this.negative = negative;
    }

    public void setPositive(int positive)
    {
        this.positive = positive;
    }

    public int getNegative()
    {
        return negative;
    }

    public int getPositive()
    {
        return positive;
    }
}
