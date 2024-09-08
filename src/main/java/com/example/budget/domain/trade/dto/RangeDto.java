package com.example.budget.domain.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.ta4j.core.num.Num;

@Getter
@ToString
@AllArgsConstructor
public class RangeDto {
    private Num min;
    private Num max;
}
