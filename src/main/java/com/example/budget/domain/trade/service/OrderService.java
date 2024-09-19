package com.example.budget.domain.trade.service;

import com.example.budget.domain.trade.dto.KlineDto;
import com.example.budget.domain.trade.model.Signal;

import java.util.List;

public interface OrderService {
    void partialDisposalTakeProfit();
    void takeProfit(List<KlineDto> klines);
    void order(Signal signal);
//    boolean isPositionExists();
    boolean isOutstandingOrderExist();
}