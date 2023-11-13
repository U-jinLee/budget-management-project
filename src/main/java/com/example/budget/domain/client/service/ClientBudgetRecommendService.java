package com.example.budget.domain.client.service;

import com.example.budget.domain.budget.repository.BudgetRepository;
import com.example.budget.domain.client.dto.BudgetRecommendDto;
import com.example.budget.domain.client.dto.CategoryTotalAmount;
import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.client.exception.ClientNotFoundException;
import com.example.budget.domain.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientBudgetRecommendService {

    private final ClientRepository clientRepo;
    private final BudgetRepository budgetRepo;

    @Transactional
    public BudgetRecommendDto.Response getTodayRecommend(long userId) {

        Client client = clientRepo.findById(userId)
                .orElseThrow(ClientNotFoundException::new);

        long amountCanSpendToday = budgetRepo.findTodayCanUseAmount(client.getEmail())
                .orElseGet(() -> 0L);

        amountCanSpendToday = (long) Math.round(amountCanSpendToday / 100.0f) * 100;

        List<CategoryTotalAmount> amountCanSpendTodayByCategory
                = budgetRepo.findCanUseAmountByCategory(client.getEmail());

        return BudgetRecommendDto.Response.from(amountCanSpendToday, amountCanSpendTodayByCategory);

    }

}
