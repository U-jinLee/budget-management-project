package com.example.budget.domain.budget.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("/api/budgets")
public class BudgetApiController {

    @PostMapping
    public ResponseEntity<Object> postBudget() {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}