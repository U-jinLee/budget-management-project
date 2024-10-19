package com.example.budget.domain.trade.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Coin {
    BTCUSDT("BTCUSDT", "USDT"),
    BTCUSDC("BTCPERP", "USDC");

    private final String value;
    private final String coinValue;
}