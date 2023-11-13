package com.example.budget.domain.budget.repository;

import com.example.budget.domain.budget.dto.BudgetCategoryAmountVo;
import com.example.budget.domain.client.dto.CategoryTotalAmount;
import com.example.budget.domain.client.dto.CategoryTotalAmountForGuide;

import java.util.List;
import java.util.Optional;

public interface BudgetRepositoryCustom {

    //해당 유저의 오늘 사용한 금액의 총량을 계산.
    Optional<Long> findTodayTotalUseAmount(String email);

    Optional<Long> findTodayCanUseAmount(String email);

    List<CategoryTotalAmount> findCanUseAmountByCategory(String email);

    //예산의 총합 구하기
    Optional<Long> findTodayTotalAmount();

    List<BudgetCategoryAmountVo> findCategoryAmount();

    List<CategoryTotalAmountForGuide> findTodayUseAmountByCategory(String email);

}
