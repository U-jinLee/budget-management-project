package com.example.budget.domain.budget.repository;

import com.example.budget.domain.budget.dto.BudgetCategoryAmountVo;
import com.example.budget.domain.client.dto.CategoryTotalAmountVo;
import com.example.budget.global.util.TimeUtil;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.budget.domain.budget.entity.QBudget.budget;
import static com.example.budget.domain.expenditure.entity.QExpenditure.expenditure;

@RequiredArgsConstructor
public class BudgetRepositoryImpl implements BudgetRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> findTodayTotalUseAmount(String email) {
        Long result = queryFactory.select(expenditure.amount.sum())
                .from(budget)
                .innerJoin(expenditure).on(expenditure.budget.eq(budget))
                .where(
                        expenditureCreatedTimeBetweenToday(),
                        budget.email.eq(email)
                ).fetchOne();

        return Optional.of(result);
    }

    @Override
    public Optional<Long> findTodayTotalAmount() {

        Long result = queryFactory.select(budget.amount.sum())
                .from(budget)
                .fetchOne();

        return Optional.of(result);
    }

    @Override
    public List<BudgetCategoryAmountVo> findCategoryAmount() {
        return queryFactory.select(
                        Projections.constructor(
                                BudgetCategoryAmountVo.class,
                                budget.category,
                                budget.amount.sum()
                        ))
                .from(budget)
                .groupBy(budget.category)
                .fetch();
    }

    @Override
    public List<CategoryTotalAmountVo> findTodayUseAmountByCategory(String email) {
        return queryFactory.select(
                        Projections.constructor(
                                CategoryTotalAmountVo.class,
                                budget.category,
                                budget.amount.subtract(budget.amountUsed).divide(TimeUtil.getRemainingDay()),
                                expenditure.amount.sum()
                        )
                )
                .from(budget)
                .innerJoin(expenditure).on(expenditure.budget.eq(budget))
                .where(
                        expenditureCreatedTimeBetweenToday(),
                        budget.email.eq(email)
                ).groupBy(budget.category).fetch();
    }

    private BooleanExpression expenditureCreatedTimeBetweenToday() {
        return expenditure.createdTime.between(TimeUtil.getToday().getStart(), TimeUtil.getToday().getEnd());
    }

}
