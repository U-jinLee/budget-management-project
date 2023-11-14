package com.example.budget.global.jwt.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class NotExistsAuthException extends BusinessException {
    public NotExistsAuthException() {
        super(ErrorCode.NOT_EXISTS_AUTH);
    }
}
