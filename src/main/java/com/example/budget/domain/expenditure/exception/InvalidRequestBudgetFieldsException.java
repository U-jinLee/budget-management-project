package com.example.budget.domain.expenditure.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class InvalidRequestBudgetFieldsException extends BusinessException {
    public InvalidRequestBudgetFieldsException() {
        super(ErrorCode.INVALID_REQUEST_BUDGET_FIELDS);
    }
}
