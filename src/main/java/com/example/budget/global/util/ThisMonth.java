package com.example.budget.global.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ThisMonth {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public static ThisMonth of(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return new ThisMonth(startDateTime, endDateTime);
    }

}
