package com.example.budget.domain.investment.service;

import com.example.budget.domain.investment.dto.TradingRecordDto;
import com.example.budget.domain.investment.entity.TradingRecord;
import org.springframework.stereotype.Component;

@Component
public class TradingRecordMapper {

    public TradingRecord toEntity(TradingRecordDto.Request request) {
        return TradingRecord.builder()
                .name(request.getName())
                .code(request.getCode())
                .tradingReason(request.getTradingReason())
                .tradingDate(request.getTradingDate())
                .tradingPrice(request.getTradingPrice())
                .tradingQuantity(request.getTradingQuantity())
                .position(request.getPosition())
                .currency(request.getCurrency())
                .build();
    }

}