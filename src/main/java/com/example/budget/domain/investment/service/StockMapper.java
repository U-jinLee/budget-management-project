package com.example.budget.domain.investment.service;

import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.investment.dto.StockDto;
import com.example.budget.domain.investment.entity.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockMapper {

    public Stock toEntity(StockDto.Request request, Client client) {
        return Stock.builder()
                .name(request.getName())
                .code(request.getCode())
                .quantity(request.getQuantity())
                .annualDividend(request.getAnnualDividend())
                .client(client)
                .build();
    }

}