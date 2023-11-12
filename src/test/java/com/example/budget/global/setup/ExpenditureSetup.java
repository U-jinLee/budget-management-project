package com.example.budget.global.setup;

import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.expenditure.entity.Expenditure;
import com.example.budget.domain.expenditure.repository.ExpenditureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExpenditureSetup {

    @Autowired
    ExpenditureRepository expenditureRepo;

    public Expenditure save(Budget budget) {
        return expenditureRepo.save(buildApplicant(budget));
    }

    private Expenditure buildApplicant(Budget budget) {
        return Expenditure.builder()
                .amount(5000L)
                .description("test description")
                .budget(budget)
                .build();
    }

}
