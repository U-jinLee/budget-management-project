package com.example.budget.domain.trade.repository;

import com.example.budget.domain.trade.model.FuturesOrder;
import com.example.budget.domain.trade.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuturesOrderRepository extends JpaRepository<FuturesOrder, Long> {
    Optional<FuturesOrder> findByOrderStatus(OrderStatus orderStatus);
    Optional<FuturesOrder> findByOrderStatusIn(List<OrderStatus> orderStatuses);
}