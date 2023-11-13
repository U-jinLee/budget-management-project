package com.example.budget.domain.budget.repository;

import com.example.budget.domain.budget.dto.BudgetCategoryAmountVo;
import com.example.budget.domain.client.dto.CategoryTotalAmount;
import com.example.budget.domain.client.dto.CategoryTotalAmountForGuide;
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
                        budgetEmailEq(email)
                ).fetchOne();

        return Optional.ofNullable(result);
    }


    @Override
    public Optional<Long> findTodayCanUseAmount(String email) {

        Long result = queryFactory.select(
                        budget.amount.sum().subtract(budget.amountUsed.sum()).divide(TimeUtil.getRemainingDay())
                )
                .from(budget)
                .where(budgetCreatedTimeBetweenThisMonth(),
                        budgetEmailEq(email))
                .fetchOne();

        return Optional.ofNullable(result);
    }


    @Override
    public List<CategoryTotalAmount> findCanUseAmountByCategory(String email) {
        return queryFactory.select(
                        Projections.constructor(
                                CategoryTotalAmount.class,
                                budget.category,
                                budget.amount.subtract(budget.amountUsed).divide(TimeUtil.getRemainingDay())
                        )
                )
                .from(budget)
                .where(
                        budgetCreatedTimeBetweenThisMonth(),
                        budgetEmailEq(email))
                .fetch();
    }


    private BooleanExpression budgetCreatedTimeBetweenThisMonth() {
        return budget.createdTime.between(TimeUtil.getThisMonth().getStartDateTime(), TimeUtil.getThisMonth().getEndDateTime());
    }


    @Override
    public Optional<Long> findTodayTotalAmount() {

        Long result = queryFactory.select(budget.amount.sum())
                .from(budget)
                .fetchOne();

        return Optional.ofNullable(result);
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
    public List<CategoryTotalAmountForGuide> findTodayUseAmountByCategory(String email) {
        return queryFactory.select(
                        Projections.constructor(
                                CategoryTotalAmountForGuide.class,
                                budget.category,
                                budget.amount.subtract(budget.amountUsed).divide(TimeUtil.getRemainingDay()),
                                expenditure.amount.sum()
                        )
                )
                .from(budget)
                .innerJoin(expenditure).on(expenditure.budget.eq(budget))
                .where(
                        expenditureCreatedTimeBetweenToday(),
                        budgetEmailEq(email)
                ).groupBy(budget.category).fetch();
    }


    private BooleanExpression budgetEmailEq(String email) {
        return budget.email.eq(email);
    }

    private BooleanExpression expenditureCreatedTimeBetweenToday() {
        return expenditure.createdTime.between(TimeUtil.getToday().getStart(), TimeUtil.getToday().getEnd());
    }

}
