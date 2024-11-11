package com.example.budget.domain.trade.model;

import com.example.budget.global.model.Leverage;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ToString
@AllArgsConstructor
@Getter
public class AccountInfoVo {
    private BigDecimal walletBalance;
    private BigDecimal balance;
    private BigDecimal cumRealisedPnl;

    /**
     * Calculate coin's order quantity
     *
     * @param positionSize Position size
     * @param markPrice Coin's mark price
     * @return Purchase quantity
     */
    public BigDecimal calculateOrderQuantity(BigDecimal positionSize, BigDecimal markPrice) {
        return balance.multiply(positionSize).divide(markPrice, 3, RoundingMode.HALF_UP)
                .multiply(Leverage.THREE.getValue());
    }

    public static AccountInfoVo newInstance(JsonObject jsonObject) {
        return new AccountInfoVo(
                jsonObject.get("walletBalance").getAsBigDecimal(),
                jsonObject.get("availableToWithdraw").getAsBigDecimal(),
                jsonObject.get("cumRealisedPnl").getAsBigDecimal());
    }

}
