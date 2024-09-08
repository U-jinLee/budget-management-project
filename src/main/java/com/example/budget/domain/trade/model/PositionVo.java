package com.example.budget.domain.trade.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ToString
@AllArgsConstructor
@Getter
public class PositionVo {
    private String symbol;
    private String side;
    private BigDecimal positionBalance;
    private BigDecimal unrealisedPnl;
    private BigDecimal size;

    public BigDecimal getRoi() {
        return this.unrealisedPnl.divide(this.positionBalance, 8, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN);
    }

    public boolean isGetPosition() {
        return !size.equals(BigDecimal.ZERO);
    }

    public static PositionVo newInstance() {
        return new PositionVo("",
                "",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO);
    }

}
