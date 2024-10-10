package com.example.budget.domain.trade.repository;

import com.example.budget.domain.trade.model.TakeProfit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakeProfitRepository extends JpaRepository<TakeProfit, Long> {
}