package com.example.budget.domain.expenditure.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class ExpenditureNotFoundException extends BusinessException {
    public ExpenditureNotFoundException() {
        super(ErrorCode.EXPENDITURE_NOT_FOUND);
    }
}
