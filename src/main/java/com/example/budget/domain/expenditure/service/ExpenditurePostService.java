package com.example.budget.domain.expenditure.service;

import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.budget.repository.BudgetRepository;
import com.example.budget.domain.expenditure.dto.ExpenditurePostDto;
import com.example.budget.domain.expenditure.exception.BudgetNotFoundException;
import com.example.budget.domain.expenditure.repository.ExpenditureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ExpenditurePostService {

    private final ExpenditureRepository expenditureRepo;
    private final BudgetRepository budgetRepo;

    @Transactional
    public ExpenditurePostDto.Response post(long budgetId, ExpenditurePostDto.Request request) {

        Budget budget = budgetRepo.findById(budgetId)
                .orElseThrow(BudgetNotFoundException::new);

        // 사용 예산 증가
        budget.plusAmountUsed(request.getAmount());

        return ExpenditurePostDto.Response.from(expenditureRepo.save(request.toEntity(budget)));
    }

}
