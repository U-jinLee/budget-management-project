package com.example.budget.domain.investment.service;

import com.example.budget.domain.investment.dto.InvestmentRatioDto;

public interface AssetsCalculateService {

    InvestmentRatioDto.Response calculateInvestmentRatio(int birthDate, long totalAssets);

    void calculate();
}
