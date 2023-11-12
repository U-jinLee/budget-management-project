package com.example.budget.domain.client.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class ClientNotFoundException extends BusinessException {
    public ClientNotFoundException() {
        super(ErrorCode.CLIENT_NOT_FOUND);
    }
}
