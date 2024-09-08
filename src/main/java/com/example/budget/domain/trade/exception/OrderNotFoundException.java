package com.example.budget.domain.trade.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class OrderNotFoundException extends BusinessException {

    public OrderNotFoundException() {
        super(ErrorCode.ORDER_NOT_FOUND);
    }
}
