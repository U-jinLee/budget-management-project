package com.example.budget.domain.expenditure.controller;

import com.example.budget.domain.expenditure.dto.ExpenditureGetDto;
import com.example.budget.domain.expenditure.dto.ExpenditurePostDto;
import com.example.budget.domain.expenditure.service.ExpenditureDeleteService;
import com.example.budget.domain.expenditure.service.ExpenditurePostService;
import com.example.budget.domain.expenditure.service.ExpenditureQueryService;
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
    private final ExpenditureQueryService expenditureQueryService;
    private final ExpenditureDeleteService expenditureDeleteService;

    /**
     * 지출 등록
     *
     * @throws com.example.budget.domain.expenditure.exception.BudgetNotFoundException 해당하는 예산이 존재하지 않으면 생기는 오류
     */
    @PostMapping
    public ResponseEntity<ExpenditurePostDto.Response> postExpenditure(@PathVariable("budgetId") long budgetId,
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

    /**
     * 지출 상세 조회
     *
     * @throws com.example.budget.domain.expenditure.exception.ExpenditureNotFoundException 해당하는 지출이 존재하지 않으면 생기는 오류
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenditureGetDto.Response> getExpenditure(@PathVariable("id") long expenditureId) {
        return ResponseEntity.status(HttpStatus.OK).body(expenditureQueryService.getExpenditure(expenditureId));
    }

    /**
     * 지출 삭제
     *
     * @throws com.example.budget.domain.expenditure.exception.ExpenditureNotFoundException 해당하는 지출이 존재하지 않으면 생기는 오류
     * @throws com.example.budget.domain.expenditure.exception.BudgetNotFoundException      해당하는 예산이 존재하지 않으면 생기는 오류
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteExpenditure(@PathVariable("budgetId") Long budgetId,
                                                    @PathVariable("id") Long id) {
        expenditureDeleteService.delete(budgetId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/exclude-total")
    public ResponseEntity<Object> excludeTotalExpenditure(@PathVariable("budgetId") long budgetId,
                                                          @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}