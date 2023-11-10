package com.example.budget.domain.expenditure.service;

import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.budget.repo.BudgetRepository;
import com.example.budget.domain.expenditure.entity.Expenditure;
import com.example.budget.domain.expenditure.exception.BudgetNotFoundException;
import com.example.budget.domain.expenditure.exception.ExpenditureNotFoundException;
import com.example.budget.domain.expenditure.repository.ExpenditureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ExpenditureDeleteService {

    private final BudgetRepository budgetRepo;
    private final ExpenditureRepository expenditureRepo;

    @Transactional
    public void delete(long budgetId, long expenditureId) {
        Expenditure expenditure = expenditureRepo.findById(expenditureId)
                .orElseThrow(ExpenditureNotFoundException::new);

        Budget budget = budgetRepo
                .findById(budgetId)
                .orElseThrow(BudgetNotFoundException::new);

        budget.minusAmountUsed(expenditure.getAmount());

        expenditureRepo.delete(expenditure);
    }

}
