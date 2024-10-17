package com.example.budget.global.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum Leverage {
    THREE(BigDecimal.valueOf(3)), TEN(BigDecimal.TEN);

    private final BigDecimal value;

    Leverage(BigDecimal value) {this.value = value;}

}
