package com.example.budget.domain.budget.service;

import com.example.budget.domain.budget.dto.BudgetDesignDto;
import com.example.budget.domain.budget.repo.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BudgetDesignService {

    private final BudgetRepository budgetRepo;

    @Transactional
    public BudgetDesignDto.Response designBudget(BudgetDesignDto.Request request) {
        return null;
    }

}
