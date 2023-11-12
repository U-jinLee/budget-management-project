package com.example.budget.domain.budget.service;

import com.example.budget.domain.budget.dto.BudgetPatchDto;
import com.example.budget.domain.budget.repository.BudgetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BudgetPatchService {

    private final BudgetRepository budgetRepo;

    @Transactional
    public void patchAmount(Long budgetId, BudgetPatchDto.Request request) {
        budgetRepo.findById(budgetId)
                .orElseThrow(EntityNotFoundException::new)
                .updateAmount(request.getAmount());
    }

}
