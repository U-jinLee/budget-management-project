package com.example.budget.domain.trade.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Coin {
    BTCUSDT("BTCUSDT"),
    BTCUSDC("BTCPERP");

    private final String value;
}