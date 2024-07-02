package com.example.budget.domain.investment.service;

import com.example.budget.domain.investment.dto.TradingRecordDto;
import com.example.budget.domain.investment.entity.TradingRecord;

public interface TradingRecordService {
    TradingRecord post(TradingRecordDto.Request request);
}
