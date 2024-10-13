package com.example.budget.domain.trade.service;

import com.example.budget.domain.trade.model.Signal;

public interface TakeProfitService {
    void execute(Signal signal);
}