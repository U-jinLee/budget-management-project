package com.example.budget.domain.budget.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class BudgetCategoryAmountVo {

    private String category;
    private long amount;

    public BudgetCategoryAmountVo(String category, long amount) {
        this.category = category;
        this.amount = amount;
    }

}
