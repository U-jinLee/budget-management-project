package com.example.budget.domain.client.service;

import com.example.budget.domain.budget.repository.BudgetRepository;
import com.example.budget.domain.client.dto.BudgetGuideDto;
import com.example.budget.domain.client.dto.CategoryTotalAmountVo;
import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.client.exception.ClientNotFoundException;
import com.example.budget.domain.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientBudgetGuideService {

    private final ClientRepository clientRepo;
    private final BudgetRepository budgetRepo;

    @Transactional(readOnly = true)
    public BudgetGuideDto.Response getTodayGuide(long userId) {
        Client client = clientRepo.findById(userId)
                .orElseThrow(ClientNotFoundException::new);

        Long totalAmount = budgetRepo.findTodayTotalUseAmount(client.getEmail())
                .orElseGet(() -> 0L);

        List<CategoryTotalAmountVo> categories =
                budgetRepo.findTodayUseAmountByCategory(client.getEmail());

        return BudgetGuideDto.Response.from(totalAmount, categories);

    }

}