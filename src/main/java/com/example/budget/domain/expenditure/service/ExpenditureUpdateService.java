package com.example.budget.domain.expenditure.service;

import com.example.budget.domain.budget.repo.BudgetRepository;
import com.example.budget.domain.expenditure.exception.ExpenditureNotFoundException;
import com.example.budget.domain.expenditure.repository.ExpenditureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ExpenditureUpdateService {

    private final BudgetRepository budgetRepo;
    private final ExpenditureRepository expenditureRepo;

    @Transactional
    public void changeContainStatus(long expenditureId) {
        expenditureRepo.findById(expenditureId)
                .orElseThrow(ExpenditureNotFoundException::new)
                .changeContainStatus();
    }

}
