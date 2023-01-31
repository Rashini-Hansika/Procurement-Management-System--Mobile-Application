package com.example.travelbee;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BudgetCalTest {
    private BudgetCal budgetCal;

    @Before
    public void setUp() throws Exception {
        budgetCal = new BudgetCal();
    }

    @Test
    public void sumTotal() {
        int result = budgetCal.sumTotal(900,100);
        assertEquals(1000, result);
    }
}