package com.example.budget.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeUtil {
    public static ThisMonth getThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate firstDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDateTime firstDateTime = LocalDateTime.of(firstDate, LocalTime.MIDNIGHT);

        int lastDay = firstDate.getMonth().maxLength();
        LocalDate lastDate = LocalDate.of(now.getYear(), now.getMonth(), lastDay);
        LocalDateTime lastDateTime = LocalDateTime.of(lastDate, LocalTime.MAX);

        return ThisMonth.of(firstDateTime, lastDateTime);
    }
}
