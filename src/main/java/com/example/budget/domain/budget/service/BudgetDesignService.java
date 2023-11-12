package com.example.budget.domain.budget.service;

import com.example.budget.domain.budget.dto.BudgetCategoryAmountVo;
import com.example.budget.domain.budget.dto.BudgetDesignDto;
import com.example.budget.domain.budget.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class BudgetDesignService {

    private final BudgetRepository budgetRepo;

    @Transactional
    public BudgetDesignDto.Response designBudget(long amount) {

        Long totalCount = budgetRepo.findTodayTotalAmount()
                .orElseGet(() -> 0L);

        List<BudgetCategoryAmountVo> categoryAmount = budgetRepo.findCategoryAmount();

        Map<String, Integer> percentage = new HashMap<>();

        for (BudgetCategoryAmountVo vo : categoryAmount) {
            int percent = (int) ((vo.getAmount() / (double) totalCount) * 100);

            log.info("percent : {}", percent);

            int value = (int) ((amount / 100.0) * percent);
            percentage.put(vo.getCategory(), value);
        }

        for (Integer i : percentage.values()) {
            log.info("value : {}", i);
        }

        List<BudgetCategoryAmountVo> result = new ArrayList<>();

        percentage.forEach((k, v) ->
                result.add(new BudgetCategoryAmountVo(k, v))
        );
        return BudgetDesignDto.Response.from(amount, result);
    }

}
