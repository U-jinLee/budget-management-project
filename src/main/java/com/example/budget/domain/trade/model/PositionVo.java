package com.example.budget.domain.trade.model;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ToString
@AllArgsConstructor
@Getter
public class PositionVo {

    private String symbol;
    private String leverage;
    private String side;
    private BigDecimal positionBalance;
    private BigDecimal unrealisedPnl;
    private BigDecimal size;
    private BigDecimal liqPrice;
    private BigDecimal avgPrice;

    public BigDecimal getRoi() {
        return this.unrealisedPnl.divide(this.positionBalance, 8, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN);
    }

    public boolean isExists() {
        return !size.equals(BigDecimal.ZERO);
    }

    public boolean sizeIsBiggerThan(BigDecimal number) {
        return this.size.compareTo(number) > 0;
    }

    public BigDecimal getHalfSize() {
        return this.size.divide(BigDecimal.valueOf(2));
    }

    @Builder(builderMethodName = "newOrderBuilder", builderClassName = "newOrderBuilder")
    public PositionVo(String side, BigDecimal positionBalance, BigDecimal size, BigDecimal avgPrice) {
        this.side = side;
        this.positionBalance = positionBalance;
        this.size = size;
        this.avgPrice = avgPrice;
    }

    public static PositionVo newInstance() {
        return new PositionVo("",
                "",
                "",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO);
    }
    public static PositionVo newInstanceFromJson(JsonObject jsonObject) {
        return new PositionVo(
                jsonObject.get("symbol").getAsString(),
                jsonObject.get("leverage").getAsString(),
                jsonObject.get("side").getAsString(),
                jsonObject.get("positionBalance").getAsBigDecimal(),
                jsonObject.get("unrealisedPnl").getAsString().equals("") ? BigDecimal.ZERO :
                        jsonObject.get("unrealisedPnl").getAsBigDecimal(),
                jsonObject.get("size").getAsBigDecimal(),
                jsonObject.get("liqPrice").getAsString().equals("") ? BigDecimal.ZERO :
                        jsonObject.get("liqPrice").getAsBigDecimal(),
                jsonObject.get("avgPrice").getAsBigDecimal());
    }

}
