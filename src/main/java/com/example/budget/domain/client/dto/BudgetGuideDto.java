package com.example.budget.domain.client.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class BudgetGuideDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        private long totalAmountSpentToday;

        private List<CategoryTotalAmountVo> categoryTotalAmounts;

        public static BudgetGuideDto.Response from(long totalAmountUsed,
                                                   List<CategoryTotalAmountVo> categoryTotalAmounts) {
            return new BudgetGuideDto.Response(totalAmountUsed, categoryTotalAmounts);
        }

    }
}
