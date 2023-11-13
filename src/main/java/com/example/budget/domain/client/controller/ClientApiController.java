package com.example.budget.domain.client.controller;

import com.example.budget.domain.client.dto.BudgetGuideDto;
import com.example.budget.domain.client.dto.BudgetRecommendDto;
import com.example.budget.domain.client.dto.SignUpDto;
import com.example.budget.domain.client.service.ClientBudgetGuideService;
import com.example.budget.domain.client.service.ClientBudgetRecommendService;
import com.example.budget.domain.client.service.ClientSignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/clients")
@RestController
public class ClientApiController {

    private final ClientSignUpService signUpService;
    private final ClientBudgetGuideService budgetGuideService;
    private final ClientBudgetRecommendService budgetRecommendService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpDto.Response> signUp(@RequestBody @Valid SignUpDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpService.signUp(request));
    }

    @GetMapping("/{clientId}/budgets/guide")
    public ResponseEntity<BudgetGuideDto.Response> getTodayGuide(@PathVariable("clientId") long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(budgetGuideService.getTodayGuide(userId));
    }

    @GetMapping("/{clientId}/budgets/recommend")
    public ResponseEntity<BudgetRecommendDto.Response> getTodayRecommend(@PathVariable("clientId") long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(budgetRecommendService.getTodayRecommend(userId));
    }

}