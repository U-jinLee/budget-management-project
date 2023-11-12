package com.example.budget.domain.budget.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BudgetDesignDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {

        @NotNull
        private Long amount;

        public static Request from(long amount) {
            return new Request(amount);
        }
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        private long totalAmount;

        public static BudgetDesignDto.Response from(long totalAmount) {
            return new BudgetDesignDto.Response(totalAmount);
        }

    }

}
