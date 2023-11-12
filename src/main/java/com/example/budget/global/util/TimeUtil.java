package com.example.budget.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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

    public static long getRemainingDay() {
        LocalDate now = LocalDate.now();

        int lastDay = now.getMonth().length(now.isLeapYear());
        LocalDate lastDayOfThisMonth = LocalDate.of(now.getYear(), now.getMonth(), lastDay);

        return ChronoUnit.DAYS.between(now, lastDayOfThisMonth);
    }

    public static StartAndEndDate getToday() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate firstDate = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth());
        LocalDateTime firstDateTime = LocalDateTime.of(firstDate, LocalTime.MIDNIGHT);

        LocalDateTime lastDateTime = LocalDateTime.of(firstDate, LocalTime.MAX);

        return StartAndEndDate.of(firstDateTime, lastDateTime);
    }
}
