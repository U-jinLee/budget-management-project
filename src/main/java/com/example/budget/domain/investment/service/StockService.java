package com.example.budget.domain.investment.service;

import com.example.budget.domain.investment.dto.StockDto;
import com.example.budget.domain.investment.entity.Stock;
import jakarta.servlet.http.HttpServletRequest;

public interface StockService {
    Stock post(StockDto.Request request, HttpServletRequest servletRequest);
}