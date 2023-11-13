package com.example.budget.domain.client.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryTotalAmountForGuide extends CategoryTotalAmount {

    private long todaySpentAmount;
    private String riskPercentage;

    public CategoryTotalAmountForGuide(String categoryName,
                                       long recommendedAmountToUseToday,
                                       long amountSpentToday) {
        super(categoryName, recommendedAmountToUseToday);
        this.todaySpentAmount = amountSpentToday;
        this.riskPercentage = Math.round((amountSpentToday / (super.getRecommendedAmountToUseToday() * 1.0f)) * 100) + "%";
    }

}
