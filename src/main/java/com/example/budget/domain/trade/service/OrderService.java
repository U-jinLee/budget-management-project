package com.example.budget.domain.trade.service;

import com.example.budget.domain.trade.model.Signal;

public interface OrderService {
    void execute(Signal signal);
}