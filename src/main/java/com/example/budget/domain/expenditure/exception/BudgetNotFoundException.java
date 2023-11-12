package com.example.budget.domain.expenditure.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class BudgetNotFoundException extends BusinessException {
    public BudgetNotFoundException() {
        super(ErrorCode.BUDGET_NOT_EXISTS);
    }
}
