package com.example.budget.domain.investment.controller;

import com.example.budget.domain.investment.dto.TradingRecordDto;
import com.example.budget.domain.investment.service.TradingRecordService;
import com.example.budget.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/investment/trading-records")
@RestController
public class TradingRecordController {

    private final TradingRecordService tradingRecordService;

    @GetMapping
    public ResponseEntity<List<TradingRecordDto.Response>> getTradingRecords() {
        List<TradingRecordDto.Response> result =
                tradingRecordService.getTradingRecords().stream().map(TradingRecordDto.Response::new).toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<TradingRecordDto.Response> postTradingRecord(
            @Valid @RequestBody TradingRecordDto.Request request,
            HttpServletRequest servletRequest) {
        TradingRecordDto.Response result = new TradingRecordDto.Response(tradingRecordService.post(request));
        URI uriPath = URI.create("/api/investment/trading-records/" + result.getId());
        return ResponseEntity.created(uriPath).body(result);
    }

}