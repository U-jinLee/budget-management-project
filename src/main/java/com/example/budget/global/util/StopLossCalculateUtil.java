package com.example.budget.global.util;

import com.bybit.api.client.domain.trade.Side;
import com.example.budget.domain.trade.model.BybitAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StopLossCalculateUtil {

    private StopLossCalculateUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String calculate(BigDecimal price, Side side) {
        return price.multiply(
                        side.equals(Side.BUY) ?
                                BigDecimal.ONE.subtract(BybitAttributes.STOP_LOSS_PERCENTAGE) :
                                BigDecimal.ONE.add(BybitAttributes.STOP_LOSS_PERCENTAGE))
                .setScale(2, RoundingMode.HALF_UP).toString();
    }

}
