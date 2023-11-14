package com.example.budget.domain.client.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class ClientPasswordNotMatchException extends BusinessException {
    public ClientPasswordNotMatchException() {
        super(ErrorCode.CLIENT_PASSWORD_NOT_MATCH);
    }
}
