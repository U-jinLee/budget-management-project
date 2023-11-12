package com.example.budget.domain.expenditure.repository;

import com.example.budget.domain.expenditure.dto.BetweenDateVo;
import com.example.budget.domain.expenditure.dto.ExpenditureSearchCondition;
import com.example.budget.domain.expenditure.entity.Expenditure;

import java.util.List;

public interface ExpenditureRepositoryCustom {
    List<Expenditure> findExpendituresBy(BetweenDateVo dateVo, ExpenditureSearchCondition condition);
}
