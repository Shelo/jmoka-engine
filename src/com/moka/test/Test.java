package com.moka.test;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<Float> floats = new ArrayList<>();
        floats.add((Math.abs(12f / 0)));

        if (floats.contains(1f / 0))
            System.out.println("OH!");
    }
}
