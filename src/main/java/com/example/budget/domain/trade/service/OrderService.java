package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.market.MarketInterval;
import com.example.budget.domain.trade.dto.KlineDto;
import com.example.budget.domain.trade.model.Signal;

import java.util.List;

public interface OrderService {
    void partialDisposalTakeProfit();
    void takeProfit();
    void order(Signal signal);
    List<KlineDto> getFuturesHistoricalKlines(MarketInterval interval, int limit, boolean isReverse);
    boolean isPositionExsits();
    boolean isOutstandingOrderExist();
}