package com.example.travelbee;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MainTODOActivityTest {
    @Test
    //test equal
    void testTitles(){
        String title1 = "E01";
        assertNotEquals("E10",title1);

        String title2 = "E02";
        assertNotEquals("E21",title2);

        String title3 =  "E03";
        assertNotEquals("E10",title3);
    }

    //test NotEqual
    void testTitleNotEquals(){
        String title1 = "E01";
        assertEquals("E01",title1);

        String title2 = "E02";
        assertEquals("E02",title2);

        String title3 = "E03";
        assertEquals("E03",title3);
    }
}