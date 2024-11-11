package com.example.budget.global.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public enum Leverage {
    THREE(BigDecimal.valueOf(3)),
    TEN(BigDecimal.TEN);

    private final BigDecimal value;

}
