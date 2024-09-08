package com.example.budget.domain.investment.service;

import com.example.budget.domain.client.repository.ClientRepository;
import com.example.budget.domain.investment.dto.TradingRecordDto;
import com.example.budget.domain.investment.entity.TradingRecord;
import com.example.budget.domain.investment.repository.TradingRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TradingRecordServiceImpl implements TradingRecordService {

    private final ClientRepository clientRepository;
    private final TradingRecordRepository tradingRecordRepo;
    private final TradingRecordMapper tradingRecordMapper;


    @Transactional(readOnly = true)
    @Override
    public List<TradingRecord> getTradingRecords() {
        return tradingRecordRepo.findAll();
    }

    @Transactional
    @Override
    public TradingRecord post(TradingRecordDto.Request request) {
        return tradingRecordRepo.save(tradingRecordMapper.toEntity(request));

    }

    @Override
    public void delete(long id) {
        tradingRecordRepo.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }


}
