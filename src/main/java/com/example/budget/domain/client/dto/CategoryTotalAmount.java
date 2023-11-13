package com.example.budget.domain.client.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryTotalAmount {
    private String categoryName;
    private long recommendedAmountToUseToday;

    public CategoryTotalAmount(String categoryName,
                               long recommendedAmountToUseToday) {
        this.categoryName = categoryName;

        this.recommendedAmountToUseToday = Math.round(recommendedAmountToUseToday / 10.0f) * 10 <= 0 ?
                1000L : Math.round(recommendedAmountToUseToday / 100.0f) * 100;
    }

}
