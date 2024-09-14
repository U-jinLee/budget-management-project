package com.example.budget.domain.trade.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Limit {
    HUNDRED(100), TWO_HUNDRED(200);

    private final int value;
}