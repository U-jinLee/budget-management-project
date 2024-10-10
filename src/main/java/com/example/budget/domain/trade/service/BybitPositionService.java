package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.example.budget.domain.trade.model.Coin;
import com.example.budget.domain.trade.model.PositionVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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

        PositionVo result = PositionVo.newInstance();

        try {
            String json = new ObjectMapper().writeValueAsString(bybitApiPositionRestClient.getPositionInfo(request));
            JsonArray jsonArray = new Gson().fromJson(json, JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonArray("list");

            if (!jsonArray.isEmpty()) {
                JsonObject jsonObject = jsonArray
                        .get(0)
                        .getAsJsonObject();

                result = new PositionVo(
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

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public BigDecimal getClosedPnL() {
        PositionDataRequest request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .build();

        BigDecimal result = BigDecimal.ZERO;

        try {
            String json = new ObjectMapper().writeValueAsString(bybitApiPositionRestClient.getClosePnlList(request));
            JsonArray jsonArray = new Gson().fromJson(json, JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonArray("list");

            for (JsonElement jsonElement : jsonArray) {
                result = result.add(jsonElement.getAsJsonObject().get("closedPnl").getAsBigDecimal());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
