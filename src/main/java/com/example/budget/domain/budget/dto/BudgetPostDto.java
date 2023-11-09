package com.example.budget.domain.budget.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

public class BudgetPostDto {

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Request {
        private Long amount;
    }

}
