package com.example.budget.global.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilTest {

    @Test
    void get_today() {
        StartAndEndDate today = TimeUtil.getToday();
        System.out.println(today.getStart() +" "+ today.getEnd());
    }

    @Test
    void getRemainingDay() {
        long result = TimeUtil.getRemainingDay();
        System.out.println(result);
    }
}