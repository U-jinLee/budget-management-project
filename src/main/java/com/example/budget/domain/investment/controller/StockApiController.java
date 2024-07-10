package com.example.budget.domain.investment.controller;

import com.example.budget.domain.investment.dto.StockDto;
import com.example.budget.domain.investment.service.StockService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/investment/stocks")
@RestController
public class StockApiController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<StockDto.Response> postStock(@RequestBody @Valid StockDto.Request request,
                                           HttpServletRequest servletRequest) {
        StockDto.Response result = new StockDto.Response(stockService.post(request, servletRequest));
        return ResponseEntity.created(URI.create("/api/investment/stock/" + result.getId())).body(result);
    }

}