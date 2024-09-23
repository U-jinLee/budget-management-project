package com.example.budget.domain.trade.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ToString
@AllArgsConstructor
@Getter
public class AccountInfoVo {
    private BigDecimal balance;

    public static AccountInfoVo newInstance() {
        return new AccountInfoVo(BigDecimal.ZERO);
    }

    /**
     * Calculate coin's order quantity
     *
     * @param positionSize Position size
     * @param markPrice Coin's mark price
     * @return Purchase quantity
     */
    public BigDecimal calculateOrderQuantity(BigDecimal positionSize, BigDecimal markPrice) {
        return balance.multiply(positionSize).divide(markPrice, 3, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(3));
    }

}
