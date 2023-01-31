package com.example.travelbee;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyCalTest {
    private CurrencyCal c_calculations;
    @Before
    public void setUp() throws Exception {
        c_calculations = new CurrencyCal();
    }
    @Test
    public void testCal1(){

        Double actual = c_calculations.convertUSDToSLruppee(10.0);
        double expected= 1982.3;
        assertEquals("Conversion is  passed", expected, actual,1982.3);

    }
    @Test
    public void testCal2(){

        Double actual = c_calculations.convertEURToSLruppee(10.0);
        double expected= 2296.9;
        assertEquals("Conversion  is passed", expected, actual,2296.9);

    }
    @Test
    public void testCal3(){

        Double actual = c_calculations.convertAUDToSLruppee(10.0);
        double expected=  1435.6;
        assertEquals("Conversion  is passed", expected, actual,1435.6);

    }
    @Test
    public void testCal4(){

        Double actual = c_calculations.convertCADToSLruppee(10.0);
        double expected=  1563.2;
        assertEquals("Conversion  is passed", expected, actual,1563.2);

    }
    @Test
    public void testCal5(){

        Double actual = c_calculations.convertINRToSLruppee(10.0);
        double expected=  26.7;
        assertEquals("Conversion  is passed", expected, actual,26.7);

    }
}