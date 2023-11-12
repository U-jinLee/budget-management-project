package com.example.budget.domain.budget.repository;

import com.example.budget.domain.client.dto.CategoryTotalAmountVo;

import java.util.List;
import java.util.Optional;

public interface BudgetRepositoryCustom {

    //해당 유저의 오늘 사용한 금액의 총량을 계산.
    Optional<Long> findTodayTotalUseAmount(String email);
    List<CategoryTotalAmountVo> findTodayUseAmountByCategory(String email);
}
