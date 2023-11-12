package com.example.budget.global.setup;

import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.budget.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BudgetSetup {

    @Autowired
    BudgetRepository budgetRepository;

    public Budget save(String category, String email) {
        return budgetRepository.save(buildApplicant(category, email));
    }

    private Budget buildApplicant(String category, String email) {
        return Budget.builder()
                .amount(1000000L)
                .category(category)
                .email(email)
                .build();
    }

}
