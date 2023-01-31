package com.example.travelbee;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceCalculationsTest {

    @Test
    public void rad2deg() {
        double input =  2;
        double output;
        double expected = 114.592;
        double delta = .1;

        DistanceCalculations distanceCalculations = new DistanceCalculations();
        output = distanceCalculations.rad2deg(input);

        assertEquals(expected, output, delta );
    }

    @Test
    public void deg2rad() {
        double input =  30;
        double output;
        double expected = 0.5236;
        double delta = .1;

        DistanceCalculations distanceCalculations = new DistanceCalculations();
        output = distanceCalculations.deg2rad(input);

        assertEquals(expected, output, delta );
    }
}