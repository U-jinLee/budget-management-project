package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.example.budget.domain.trade.model.Coin;
import com.example.budget.domain.trade.model.PositionVo;
import com.example.budget.global.util.JsonParsingUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

        JsonObject jsonObject = JsonParsingUtil.parsingJson(bybitApiPositionRestClient.getPositionInfo(request));

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

    public BigDecimal getClosedPnL() {
        PositionDataRequest request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .build();

        BigDecimal result = BigDecimal.ZERO;

        JsonArray jsonArray = JsonParsingUtil.parsingJsonArray(bybitApiPositionRestClient.getClosePnlList(request));

        for (JsonElement jsonElement : jsonArray) {
            result = result.add(jsonElement.getAsJsonObject().get("closedPnl").getAsBigDecimal());
        }

        return result.setScale(2, RoundingMode.HALF_UP);
    }

}
