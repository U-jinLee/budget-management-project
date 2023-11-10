package com.example.budget.domain.expenditure.service;

import com.example.budget.domain.expenditure.dto.ExpenditureGetDto;
import com.example.budget.domain.expenditure.exception.ExpenditureNotFoundException;
import com.example.budget.domain.expenditure.repository.ExpenditureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ExpenditureQueryService {

    private final ExpenditureRepository expenditureRepo;

    @Transactional(readOnly = true)
    public ExpenditureGetDto.Response getExpenditure(Long expenditureId) {
        return ExpenditureGetDto.Response.from(expenditureRepo.findById(expenditureId)
                .orElseThrow(ExpenditureNotFoundException::new));
    }

}
