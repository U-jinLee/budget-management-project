package com.example.budget.domain.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ta4j.core.num.Num;

@Getter
@AllArgsConstructor
public class MinAndMaxDto {
    private Num min;
    private Num max;
}