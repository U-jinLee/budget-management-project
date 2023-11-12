package com.example.budget.domain.client.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryTotalAmountVo {
    private String categoryName;
    private long recommendedAmountToUseToday;
    private long amountSpentToday;
    private String riskPercentage;

    public CategoryTotalAmountVo(String categoryName,
                                 long recommendedAmountToUseToday,
                                 long amountSpentToday) {
        this.categoryName = categoryName;
        this.recommendedAmountToUseToday = Math.round(recommendedAmountToUseToday / 10.0f) * 10;
        this.amountSpentToday =amountSpentToday;
        this.riskPercentage = Math.round((amountSpentToday / (recommendedAmountToUseToday * 1.0f)) * 100) + "%";
    }

}