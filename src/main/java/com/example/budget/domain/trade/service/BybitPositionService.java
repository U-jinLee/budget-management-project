package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.example.budget.domain.trade.model.Coin;
import com.example.budget.domain.trade.model.PositionVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class BybitPositionService {

    private final BybitApiPositionRestClient bybitApiPositionRestClient;

    public PositionVo getPositionInfo() {

        PositionDataRequest request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .build();

        PositionVo result = PositionVo.newInstance();

        try {
            String json = new ObjectMapper().writeValueAsString(bybitApiPositionRestClient.getPositionInfo(request));

            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonArray("list")
                    .get(0)
                    .getAsJsonObject();

            result = new PositionVo(
                    jsonObject.get("symbol").getAsString(),
                    jsonObject.get("side").getAsString(),
                    jsonObject.get("positionBalance").getAsBigDecimal(),
                    jsonObject.get("unrealisedPnl").getAsString().equals("") ? BigDecimal.ZERO :
                            jsonObject.get("unrealisedPnl").getAsBigDecimal(),
                    jsonObject.get("size").getAsBigDecimal());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }
}
