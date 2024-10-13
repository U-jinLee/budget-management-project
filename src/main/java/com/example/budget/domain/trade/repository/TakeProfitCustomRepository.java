package com.example.budget.domain.trade.repository;

import java.math.BigDecimal;

public interface TakeProfitCustomRepository {
    BigDecimal sumRealizedPnl();
}
