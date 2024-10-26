package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.example.budget.domain.trade.model.Coin;
import com.example.budget.domain.trade.model.PositionVo;
import com.example.budget.global.util.JsonParsingUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
@RequiredArgsConstructor
public class BybitPositionService {

    private final BybitApiPositionRestClient bybitApiPositionRestClient;

    /**
     * Get bybit BTCUSDT position information
     *
     * @return Bybit position information
     */
    public PositionVo getPositionInfo() {

        PositionDataRequest request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .build();

        return PositionVo
                .newInstanceFromJson(JsonParsingUtil
                        .parsingToJson(bybitApiPositionRestClient.getPositionInfo(request)));
    }

    public BigDecimal getClosedPnL() {
        PositionDataRequest request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .build();

        BigDecimal result = BigDecimal.ZERO;

        JsonArray jsonArray = JsonParsingUtil.parsingJsonArray(bybitApiPositionRestClient.getClosePnlList(request));

        if (!jsonArray.isEmpty()) {
            for (JsonElement jsonElement : jsonArray) {
                result = result.add(jsonElement.getAsJsonObject().get("closedPnl").getAsBigDecimal());
            }
        }

        return result.setScale(2, RoundingMode.HALF_UP);
    }

}
