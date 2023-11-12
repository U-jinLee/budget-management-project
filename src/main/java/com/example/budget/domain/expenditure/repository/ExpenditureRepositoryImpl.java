package com.example.budget.domain.expenditure.repository;

import com.example.budget.domain.expenditure.dto.BetweenDateVo;
import com.example.budget.domain.expenditure.dto.ExpenditureSearchCondition;
import com.example.budget.domain.expenditure.entity.Expenditure;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.budget.domain.budget.entity.QBudget.budget;
import static com.example.budget.domain.expenditure.entity.QExpenditure.expenditure;

@RequiredArgsConstructor
public class ExpenditureRepositoryImpl implements ExpenditureRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Expenditure> findExpendituresBy(BetweenDateVo dateVo, ExpenditureSearchCondition condition) {
        return queryFactory.select(expenditure)
                .from(expenditure)
                .innerJoin(budget).on(expenditure.budget.eq(budget))
                .where(categoryEq(condition.getCategory()))
                .where(createdTimeBetween(dateVo))
                .fetch();
    }

    private BooleanExpression createdTimeBetween(BetweenDateVo dateVo) {
        return expenditure.createdTime.between(dateVo.getStartDate(), dateVo.getEndDate());
    }

    private BooleanExpression categoryEq(String category) {
        return StringUtils.isNullOrEmpty(category) ? null : budget.category.eq(category);
    }

}