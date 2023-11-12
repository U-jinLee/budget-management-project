package com.example.budget.domain.expenditure.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class StartDateIsBeforeEndDateException extends BusinessException {
    public StartDateIsBeforeEndDateException() {
        super(ErrorCode.START_DATE_IS_BEFORE_END_DATE);
    }
}
