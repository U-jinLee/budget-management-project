package com.example.budget.domain.trade.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class SignalUnknownException extends BusinessException {

    public SignalUnknownException() {
        super(ErrorCode.TRADE_SIGNAL_UNKNOWN);
    }
    
}
