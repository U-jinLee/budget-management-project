package com.example.budget.domain.investment.repository;

import com.example.budget.domain.investment.entity.TradingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingRecordRepository extends JpaRepository<TradingRecord, Long> {

}