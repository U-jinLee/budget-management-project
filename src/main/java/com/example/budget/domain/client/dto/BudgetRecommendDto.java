package com.example.budget.domain.client.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BudgetRecommendDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        private long amountCanSpendToday;
        private List<CategoryTotalAmount> categoryTotalAmounts;

        public static BudgetRecommendDto.Response from(long amountCanSpendToday,
                                                       List<CategoryTotalAmount> categoryTotalAmounts) {
            return new BudgetRecommendDto.Response(amountCanSpendToday, categoryTotalAmounts);
        }

    }

}
