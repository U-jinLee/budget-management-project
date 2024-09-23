package com.example.budget.domain.trade.exception;

import com.example.budget.global.error.exception.BusinessException;
import com.example.budget.global.error.exception.ErrorCode;

public class PositionIsLiquidatedException extends BusinessException {

    public PositionIsLiquidatedException() {
        super(ErrorCode.POSITION_IS_LIQUIDATED);
    }
}
