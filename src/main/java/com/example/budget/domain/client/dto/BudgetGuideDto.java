package com.example.budget.domain.client.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BudgetGuideDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Response {

        private long totalAmountSpentToday;

        private List<CategoryTotalAmountForGuide> categoryTotalAmounts;

        public static BudgetGuideDto.Response from(long totalAmountUsed,
                                                   List<CategoryTotalAmountForGuide> categoryTotalAmounts) {
            return new BudgetGuideDto.Response(totalAmountUsed, categoryTotalAmounts);
        }

    }
}
