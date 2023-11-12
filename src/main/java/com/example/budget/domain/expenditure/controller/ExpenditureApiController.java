package com.example.budget.domain.expenditure.controller;

import com.example.budget.domain.expenditure.dto.*;
import com.example.budget.domain.expenditure.entity.Expenditure;
import com.example.budget.domain.expenditure.service.ExpenditureDeleteService;
import com.example.budget.domain.expenditure.service.ExpenditurePostService;
import com.example.budget.domain.expenditure.service.ExpenditureQueryService;
import com.example.budget.domain.expenditure.service.ExpenditureUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/budgets/{budgetId}/expenditures")
@RestController
public class ExpenditureApiController {

    private final ExpenditurePostService expenditurePostService;
    private final ExpenditureQueryService expenditureQueryService;
    private final ExpenditureDeleteService expenditureDeleteService;
    private final ExpenditureUpdateService expenditureUpdateService;

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

    /**
     * 지출 수정
     *
     * @throws com.example.budget.domain.expenditure.exception.BudgetNotFoundException      해당하는 예산이 존재하지 않으면 생기는 오류
     * @throws com.example.budget.domain.expenditure.exception.ExpenditureNotFoundException 해당하는 지출이 존재하지 않으면 생기는 오류
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> putExpenditure(@PathVariable("budgetId") long budgetId,
                                                 @PathVariable("id") long expenditureId,
                                                 @RequestBody ExpenditurePutDto.Request request) {
        expenditureUpdateService.putExpenditure(budgetId, expenditureId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 지출 조회
     */
    @GetMapping
    public ResponseEntity<List<Expenditure>> getExpenditures(
            @RequestParam(value = "start-date") LocalDateTime startDate,
            @RequestParam(value = "end-date") LocalDateTime endDate,
            ExpenditureSearchCondition condition) {

        return ResponseEntity.status(HttpStatus.OK).body(
                expenditureQueryService.getExpenditures(
                        BetweenDateVo.from(startDate, endDate),
                        condition
                )
        );

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
    public ResponseEntity<Object> deleteExpenditure(@PathVariable("budgetId") long budgetId,
                                                    @PathVariable("id") Long id) {
        expenditureDeleteService.delete(budgetId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 지출 합계 여부 변경
     *
     * @throws com.example.budget.domain.expenditure.exception.ExpenditureNotFoundException 해당하는 지출이 존재하지 않으면 생기는 오류
     */
    @PatchMapping("/{id}/exclude-total")
    public ResponseEntity<Object> excludeTotalExpenditure(@PathVariable("id") long id) {
        expenditureUpdateService.changeContainStatus(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}