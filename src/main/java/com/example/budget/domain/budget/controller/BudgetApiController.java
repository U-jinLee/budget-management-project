package com.example.budget.domain.budget.controller;

import com.example.budget.domain.budget.dto.BudgetPostDto;
import com.example.budget.domain.budget.service.BudgetPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/budgets")
@RestController
public class BudgetApiController {

    private final BudgetPostService budgetPostService;

    /**
     * @exception com.example.budget.domain.budget.exception.BudgetAlreadyExistsException 해당하는 예산 설정이 존재할 경우 발생하는 오류
     */
    @PostMapping
    public ResponseEntity<BudgetPostDto.Response> postBudget(@RequestBody @Valid BudgetPostDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetPostService.post(request));
    }

}