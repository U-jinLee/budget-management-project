package com.example.budget.domain.investment.service;

import com.example.budget.domain.investment.dto.TradingRecordDto;
import com.example.budget.domain.investment.entity.TradingRecord;
import com.example.budget.domain.investment.repository.TradingRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TradingRecordServiceImpl implements TradingRecordService {

    private final TradingRecordRepository tradingRecordRepo;
    private final TradingRecordMapper tradingRecordMapper;

    @Transactional
    public TradingRecord post(TradingRecordDto.Request request) {
        return tradingRecordRepo.save(tradingRecordMapper.toEntity(request));
    }

}
