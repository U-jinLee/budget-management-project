package com.example.budget.domain.expenditure.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class MinIsBiggetThanMaxException extends BusinessException {
    public MinIsBiggetThanMaxException() {
        super(ErrorCode.MIN_IS_BIGGER_THAN_MAX);
    }
}
