package com.example.budget.domain.budget.service;

import com.example.budget.domain.budget.dto.BudgetPostDto;
import com.example.budget.domain.budget.exception.BudgetAlreadyExistsException;
import com.example.budget.domain.budget.repository.BudgetRepository;
import com.example.budget.domain.category.repo.CategoryRepository;
import com.example.budget.domain.client.repository.ClientRepository;
import com.example.budget.domain.expenditure.exception.InvalidRequestBudgetFieldsException;
import com.example.budget.global.util.ThisMonth;
import com.example.budget.global.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BudgetPostService {

    private final BudgetRepository budgetRepo;
    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public BudgetPostDto.Response post(BudgetPostDto.Request request) {
        if(validateRequestFields(request))
            throw new InvalidRequestBudgetFieldsException();

        if(alreadyHasBudget(request))
            throw new BudgetAlreadyExistsException();

        return BudgetPostDto.Response.from(budgetRepo.save(request.toEntity()));
    }

    private boolean validateRequestFields(BudgetPostDto.Request request) {
        return !categoryRepository.existsByName(request.getCategory()) || !clientRepository.existsByEmail(request.getEmail());
    }

    private boolean alreadyHasBudget(BudgetPostDto.Request request) {

        ThisMonth thisMonth = TimeUtil.getThisMonth();

        return budgetRepo.findByCreatedTimeBetweenAndEmailAndCategory(
                thisMonth.getStartDateTime(),
                thisMonth.getEndDateTime(),
                request.getEmail(),
                request.getCategory()).isPresent();

    }

}
