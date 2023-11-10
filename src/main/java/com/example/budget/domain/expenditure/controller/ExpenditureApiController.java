package com.example.budget.domain.expenditure.controller;

import com.example.budget.domain.expenditure.dto.ExpenditurePostDto;
import com.example.budget.domain.expenditure.service.ExpenditurePostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/budgets/{budgetId}/expenditures")
@RestController
public class ExpenditureApiController {

    private final ExpenditurePostService expenditurePostService;

    /**
     * 지출 등록
     *
     * @exception com.example.budget.domain.expenditure.exception.BudgetNotFoundException 해당하는 예산이 존재하지 않으면 생기는 오류
     */
    @PostMapping
    public ResponseEntity<ExpenditurePostDto.Response> postExpenditure(@PathVariable("budgetId") Long budgetId,
                                                  @RequestBody @Valid ExpenditurePostDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenditurePostService.post(budgetId, request));
    }

    @PutMapping
    public ResponseEntity<Object> putExpenditure(@PathVariable("budgetId") Long budgetId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<Object> getExpenditures(@PathVariable("budgetId") Long budgetId) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getExpenditure(@PathVariable("budgetId") Long budgetId,
                                                 @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteExpenditure(@PathVariable("budgetId") Long budgetId,
                                                 @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/exclude-total")
    public ResponseEntity<Object> excludeTotalExpenditure(@PathVariable("budgetId") Long budgetId,
                                                    @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}