package com.example.budget.domain.trade.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

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

    public boolean isExists() {
        return !size.equals(BigDecimal.ZERO);
    }

    public boolean sizeIsBiggerThan(BigDecimal number) {
        return this.size.compareTo(number) > 0 ? true : false;
    }

    public static PositionVo newInstance() {
        return new PositionVo("",
                "",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionVo that = (PositionVo) o;
        return Objects.equals(symbol, that.symbol) &&
                Objects.equals(side, that.side) &&
                Objects.equals(positionBalance, that.positionBalance) &&
                Objects.equals(unrealisedPnl, that.unrealisedPnl) &&
                Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, side, positionBalance, unrealisedPnl, size);
    }
}
