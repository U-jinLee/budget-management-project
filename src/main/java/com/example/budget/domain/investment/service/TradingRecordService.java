package com.example.budget.domain.investment.service;

import com.example.budget.domain.investment.dto.TradingRecordDto;
import com.example.budget.domain.investment.entity.TradingRecord;

import java.util.List;

public interface TradingRecordService {
    List<TradingRecord> getTradingRecords();

    TradingRecord post(TradingRecordDto.Request request);

    void delete(long id);
}