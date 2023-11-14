package com.example.budget.domain.client.controller;

import com.example.budget.domain.client.dto.BudgetGuideDto;
import com.example.budget.domain.client.dto.BudgetRecommendDto;
import com.example.budget.domain.client.service.ClientBudgetGuideService;
import com.example.budget.domain.client.service.ClientBudgetRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/clients")
@RestController
public class ClientApiController {

    private final ClientBudgetGuideService budgetGuideService;
    private final ClientBudgetRecommendService budgetRecommendService;

    @GetMapping("/{clientId}/budgets/guide")
    public ResponseEntity<BudgetGuideDto.Response> getTodayGuide(@PathVariable("clientId") long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(budgetGuideService.getTodayGuide(userId));
    }

    @GetMapping("/{clientId}/budgets/recommend")
    public ResponseEntity<BudgetRecommendDto.Response> getTodayRecommend(@PathVariable("clientId") long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(budgetRecommendService.getTodayRecommend(userId));
    }

}