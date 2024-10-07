package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.TradeOrderType;
import com.bybit.api.client.domain.position.TpslMode;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.example.budget.domain.trade.model.Coin;
import com.example.budget.domain.trade.model.PositionVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class BybitTradeService {

    private final BybitApiTradeRestClient bybitApiTradeRestClient;

    public void cancelAllOrder() {
        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .build();

        bybitApiTradeRestClient.cancelAllOrder(request);
    }

    public void takeProfit(Side side, String price, String quantity) {
        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .side(side)
                .price(price)
                .qty(quantity)
                .orderType(TradeOrderType.LIMIT)
                .build();

        bybitApiTradeRestClient.createOrder(request);
    }

    public void createOrder(Side side, BigDecimal price, BigDecimal stopLoss, BigDecimal orderQty) {
        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .side(side)
                .orderType(TradeOrderType.LIMIT)
                .price(price.toString())
                .tpslMode(TpslMode.PARTIAL.getDescription())
                .stopLoss(stopLoss.toString())
                .qty(orderQty.toString())
                .build();

        bybitApiTradeRestClient.createOrder(request);
    }

    public PositionVo getOpenOrder() {
        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .openOnly(0)
                .build();
        PositionVo positionVo = PositionVo.newInstance();
        try {
            String jsonString = new ObjectMapper().writeValueAsString(bybitApiTradeRestClient.getOpenOrders(request));
            JsonArray jsonArray = new Gson().fromJson(jsonString, JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonArray("list");
            if (jsonArray.size() != 0) {
                JsonObject jsonObject = jsonArray.get(0)
                        .getAsJsonObject();
                positionVo = PositionVo.newOrderBuilder()
                        .avgPrice(jsonObject.get("price").getAsBigDecimal())
                        .positionBalance(jsonObject.get("leavesValue").getAsBigDecimal())
                        .side(jsonObject.get("side").getAsString())
                        .size(jsonObject.get("qty").getAsBigDecimal())
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return positionVo;
    }

}
