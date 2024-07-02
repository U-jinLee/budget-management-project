package com.example.budget.domain.investment.controller;

import com.example.budget.domain.investment.dto.TradingRecordDto;
import com.example.budget.domain.investment.service.TradingRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/investment/trading-records")
@RestController
public class TradingRecordController {

    private final TradingRecordService tradingRecordService;

    @PostMapping
    public ResponseEntity<TradingRecordDto.Response> postTradingRecord(
            @Valid @RequestBody TradingRecordDto.Request request) {
        TradingRecordDto.Response result = new TradingRecordDto.Response(tradingRecordService.post(request));
        return ResponseEntity.created(URI.create("/api/investment/trading-records/" + result.getId())).body(result);
    }

}