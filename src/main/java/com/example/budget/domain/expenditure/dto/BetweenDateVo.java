package com.example.budget.domain.expenditure.dto;

import com.example.budget.domain.expenditure.exception.StartDateIsBeforeEndDateException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BetweenDateVo {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public static BetweenDateVo from(LocalDateTime startDate,
                                     LocalDateTime endDate) {
        validate(startDate, endDate);
        return new BetweenDateVo(startDate, endDate);
    }

    private static void validate(LocalDateTime startDate, LocalDateTime endDate) {
        // 시작일이 종료일보다 늦을 경우 예외처리
        if (startDate.isAfter(endDate)) throw new StartDateIsBeforeEndDateException();
    }
}
