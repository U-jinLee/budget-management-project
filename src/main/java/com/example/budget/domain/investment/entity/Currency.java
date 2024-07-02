package com.example.budget.domain.investment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {
    KRW("대한민국 원", "₩"),
    USD("달러", "$");

    private String description;
    private String symbol;
}