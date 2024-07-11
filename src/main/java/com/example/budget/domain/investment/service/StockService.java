package com.example.budget.domain.investment.service;

import com.example.budget.domain.investment.dto.StockDto;
import com.example.budget.domain.investment.entity.Stock;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockService {

    Page<Stock> getStocks(HttpServletRequest servletRequest, Pageable pageable);

    Stock post(StockDto.Request request, HttpServletRequest servletRequest);
}