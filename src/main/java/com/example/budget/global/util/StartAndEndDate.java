package com.example.budget.global.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StartAndEndDate {
    private LocalDateTime start;
    private LocalDateTime end;

    public static StartAndEndDate of(LocalDateTime start, LocalDateTime end) {
        return new StartAndEndDate(start, end);
    }

}
