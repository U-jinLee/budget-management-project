package com.example.budget.domain.trade.repository;

import com.example.budget.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TakeProfitCustomRepositoryTest extends IntegrationTest {

    @Autowired
    TakeProfitRepository takeProfitRepository;

    @Test
    @DisplayName("take realized pnl from take profit entity")
    void sumRealizedPnl() {
        BigDecimal actual = takeProfitRepository.sumRealizedPnl();
        assertNotEquals(null, actual);
    }
}