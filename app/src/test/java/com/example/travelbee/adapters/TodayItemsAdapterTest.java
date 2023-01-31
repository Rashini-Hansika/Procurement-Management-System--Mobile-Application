package com.example.travelbee.adapters;

import static org.junit.Assert.*;

import org.junit.Test;

public class TodayItemsAdapterTest {
    @Test
    //test equal
    public void itemEquals() {
        String item1 = "Cement";
        assertEquals ("Cement",item1);

        String item2 = "Sand";
        assertEquals ("Sand",item2);

        String item3 = "Concrete";
        assertEquals ("Concrete",item3);
    }

    //test not equal
    public void itemNotEquals() {
        String item1 = "Cement";
        assertNotEquals ("Plastic",item1);

        String item2 = "Sand";
        assertNotEquals ("Plastic",item1);

        String item3 = "Concrete";
        assertNotEquals ("Plastic",item1);
    }

}