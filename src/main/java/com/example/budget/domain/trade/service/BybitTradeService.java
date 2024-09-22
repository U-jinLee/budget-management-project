package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.TradeOrderType;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.example.budget.domain.trade.model.Coin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    public void createOrder(Side side, String quantity, TradeOrderType tradeOrderType) {
        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .side(side)
                .qty(quantity)
                .orderType(tradeOrderType)
                .build();

        bybitApiTradeRestClient.createOrder(request);
    }

    public void createOrder(Side side, String price, String quantity) {
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

}
