package com.example.budget.domain.investment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvestmentRatioDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private Integer birthDate;
        private Long totalAssets;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long cashAmount;
        private Long investmentAmount;
    }

}