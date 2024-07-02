package com.example.budget.domain.investment.controller;

import com.example.budget.domain.investment.dto.InvestmentRatioDto;
import com.example.budget.domain.investment.service.AssetsCalculateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/investment/assets")
@RestController
public class AssetsController {

    private final AssetsCalculateService assetsCalculateService;

    @GetMapping("/investment-ratio")
    public ResponseEntity<InvestmentRatioDto.Response> getInvestmentRatio(
            @RequestBody InvestmentRatioDto.Request request) {

        return ResponseEntity.ok().body(assetsCalculateService
                .calculateInvestmentRatio(request.getBirthDate(), request.getTotalAssets()));
    }

    @GetMapping
    public ResponseEntity<Object> postAssets() {
        assetsCalculateService.calculate();
        return ResponseEntity.created(null).body(null);
    }

}