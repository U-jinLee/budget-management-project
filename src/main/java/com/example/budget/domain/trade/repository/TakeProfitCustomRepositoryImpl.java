package com.example.budget.domain.trade.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.example.budget.domain.trade.model.QTakeProfit.takeProfit;

@RequiredArgsConstructor
public class TakeProfitCustomRepositoryImpl implements TakeProfitCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public BigDecimal sumRealizedPnl() {
        return queryFactory.select(takeProfit.realizedPnl.sum())
                .from(takeProfit)
                .fetchFirst()
                .setScale(2, RoundingMode.HALF_UP);
    }
}
