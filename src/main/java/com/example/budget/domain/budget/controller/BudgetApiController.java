package com.example.budget.domain.budget.controller;

import com.example.budget.domain.budget.dto.BudgetDesignDto;
import com.example.budget.domain.budget.dto.BudgetPatchDto;
import com.example.budget.domain.budget.dto.BudgetPostDto;
import com.example.budget.domain.budget.service.BudgetDesignService;
import com.example.budget.domain.budget.service.BudgetPatchService;
import com.example.budget.domain.budget.service.BudgetPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/budgets")
@RestController
public class BudgetApiController {

    private final BudgetPostService budgetPostService;
    private final BudgetPatchService budgetPatchService;
    private final BudgetDesignService budgetDesignService;

    /**
     * 이번 달 예산 등록
     *
     * @exception com.example.budget.domain.budget.exception.BudgetAlreadyExistsException 해당하는 예산 설정이 존재할 경우 발생하는 오류
     */
    @PostMapping
    public ResponseEntity<BudgetPostDto.Response> postBudget(@RequestBody @Valid BudgetPostDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetPostService.post(request));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchBudgetAmount(@PathVariable("id") Long id,
                                                    @RequestBody @Valid BudgetPatchDto.Request request) {
        budgetPatchService.patchAmount(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/design")
    public ResponseEntity<Object> designBudget(@RequestBody @Valid BudgetDesignDto.Request request) {
        return ResponseEntity.status(HttpStatus.OK).body(budgetDesignService.designBudget(request));
    }

}