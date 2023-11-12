package com.example.budget.domain.expenditure.service;

import com.example.budget.domain.expenditure.dto.BetweenDateVo;
import com.example.budget.domain.expenditure.dto.ExpenditureGetDto;
import com.example.budget.domain.expenditure.dto.ExpenditureSearchCondition;
import com.example.budget.domain.expenditure.entity.Expenditure;
import com.example.budget.domain.expenditure.exception.ExpenditureNotFoundException;
import com.example.budget.domain.expenditure.repository.ExpenditureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExpenditureQueryService {

    private final ExpenditureRepository expenditureRepo;

    public ExpenditureGetDto.Response getExpenditure(Long expenditureId) {
        return ExpenditureGetDto.Response.from(expenditureRepo.findById(expenditureId)
                .orElseThrow(ExpenditureNotFoundException::new));
    }

    public List<Expenditure> getExpenditures(BetweenDateVo dateVo, ExpenditureSearchCondition condition) {
        return expenditureRepo.findExpendituresBy(dateVo, condition);
    }

}
