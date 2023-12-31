package com.example.budget.domain.expenditure.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpenditureSearchCondition {
    private String category;
    private Integer min;
    private Integer max;
}