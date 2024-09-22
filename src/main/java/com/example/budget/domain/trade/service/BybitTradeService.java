package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.example.budget.domain.trade.model.Coin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BybitTradeService {

    private final BybitApiTradeRestClient bybitApiTradeRestClient;

    @Transactional
    public void cancelAllOrder() {
        TradeOrderRequest request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .build();

        bybitApiTradeRestClient.cancelAllOrder(request);
    }
}
