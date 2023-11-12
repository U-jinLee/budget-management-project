package com.example.budget.domain.expenditure.service;

import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.budget.repo.BudgetRepository;
import com.example.budget.domain.expenditure.dto.ExpenditurePutDto;
import com.example.budget.domain.expenditure.entity.Expenditure;
import com.example.budget.domain.expenditure.exception.BudgetNotFoundException;
import com.example.budget.domain.expenditure.exception.ExpenditureNotFoundException;
import com.example.budget.domain.expenditure.repository.ExpenditureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ExpenditureUpdateService {

    private final BudgetRepository budgetRepo;
    private final ExpenditureRepository expenditureRepo;

    @Transactional
    public void putExpenditure(long budgetId,
                               long expenditureId,
                               ExpenditurePutDto.Request request) {
        // 예산, 지출 조회
        Budget budget = budgetRepo.findById(budgetId)
                .orElseThrow(BudgetNotFoundException::new);
        Expenditure expenditure = expenditureRepo.findById(expenditureId)
                .orElseThrow(ExpenditureNotFoundException::new);
        // 예산 사용 금액 변경
        changeBudgetAmountUsed(request, expenditure, budget);
        // 지출 수정
        expenditure.updateTo(request);
    }

    private void changeBudgetAmountUsed(ExpenditurePutDto.Request request, Expenditure expenditure, Budget budget) {
        long difference = 0L;

        // 현재 지출보다 큰 금액으로 수정될 경우
        if (expenditure.getAmount() < request.getAmount()) {
            difference = request.getAmount() - expenditure.getAmount();
            // 사용 예산 증가
            budget.plusAmountUsed(difference);
        }
        // 현재 지출보다 작은 금액으로 수정될 경우
        else if (expenditure.getAmount() > request.getAmount()) {
            // 사용 예산 감소
            difference = expenditure.getAmount() - request.getAmount();
            budget.minusAmountUsed(difference);
        }

    }

    @Transactional
    public void changeContainStatus(long expenditureId) {
        expenditureRepo.findById(expenditureId)
                .orElseThrow(ExpenditureNotFoundException::new)
                .changeContainStatus();
    }

}
