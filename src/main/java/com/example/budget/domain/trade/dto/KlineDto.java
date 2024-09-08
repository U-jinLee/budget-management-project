package com.example.budget.domain.trade.dto;

import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONArray;

import java.math.BigDecimal;

@Getter
@ToString
@AllArgsConstructor
public class KlineDto {
    private long openTime;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private BigDecimal volume;
    private long closeTime;

    public static KlineDto newInstance(JSONArray jsonArray) {
        return new KlineDto(
                jsonArray.getLong(0),
                jsonArray.getBigDecimal(1),
                jsonArray.getBigDecimal(2),
                jsonArray.getBigDecimal(3),
                jsonArray.getBigDecimal(4),
                jsonArray.getBigDecimal(5),
                jsonArray.getLong(6)
        );
    }

    public static KlineDto newInstance(JsonArray jsonArray) {
        return new KlineDto(
                jsonArray.get(0).getAsLong(),
                jsonArray.get(1).getAsBigDecimal(),
                jsonArray.get(2).getAsBigDecimal(),
                jsonArray.get(3).getAsBigDecimal(),
                jsonArray.get(4).getAsBigDecimal(),
                jsonArray.get(5).getAsBigDecimal(),
                jsonArray.get(0).getAsLong()
        );
    }

}