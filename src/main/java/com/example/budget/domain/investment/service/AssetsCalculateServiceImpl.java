package com.example.budget.domain.investment.service;

import com.example.budget.domain.investment.dto.InvestmentRatioDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class AssetsCalculateServiceImpl implements AssetsCalculateService {

    @Override
    public InvestmentRatioDto.Response calculateInvestmentRatio(int birthDate, long totalAssets) {
        int cashRatio = LocalDate.now().getYear() - birthDate;
        int investmentRatio = 100 - cashRatio;
        float cash = (float) cashRatio / 100;
        float investment = (float) investmentRatio / 100;
        return new InvestmentRatioDto
                .Response((long) (totalAssets * cash), (long) (totalAssets * investment));
    }

    @Override
    public void calculate() {
        int birthYear = 1993;
        int thisYear = LocalDate.now().getYear();
        log.info("my age: {}", thisYear - birthYear);
    }

}