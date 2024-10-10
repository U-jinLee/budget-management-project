package com.example.budget.domain.trade.repository;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.trade.model.FuturesOrder;
import com.example.budget.domain.trade.model.OrderStatus;
import com.example.budget.domain.trade.model.Signal;
import com.example.budget.domain.trade.model.TakeProfit;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

class TakeProfitRepositoryTest extends IntegrationTest {
    @Autowired
    FuturesOrderRepository futuresOrderRepository;
    @Autowired
    TakeProfitRepository takeProfitRepository;

    @Test
    @DisplayName("Create take profit entity")
    void createTakeProfit() {
        FuturesOrder save = this.futuresOrderRepository.save(
                FuturesOrder.builder()
                        .orderNumber(1)
                        .orderSignal(Signal.GREEN)
                        .orderStatus(OrderStatus.CANCELED)
                        .build()
        );

        FuturesOrder futuresOrder = this.futuresOrderRepository.findById(save.getId())
                .orElseThrow(EntityNotFoundException::new);

        TakeProfit takeProfit = TakeProfit.builder()
                .price(BigDecimal.valueOf(66000.30))
                .quantity(BigDecimal.valueOf(0.006))
                .futuresOrder(futuresOrder)
                .build();

        takeProfit = takeProfitRepository.save(takeProfit);
        Assertions.assertNotEquals(null, takeProfit.getId());
    }
}