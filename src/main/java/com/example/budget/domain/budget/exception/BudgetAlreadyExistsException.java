package com.example.budget.domain.budget.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class BudgetAlreadyExistsException extends BusinessException {

    public BudgetAlreadyExistsException() {
        super(ErrorCode.BUDGET_ALREADY_EXISTS);
    }
}
