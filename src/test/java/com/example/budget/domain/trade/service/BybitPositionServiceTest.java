package com.example.budget.domain.trade.service;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.trade.model.PositionVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BybitPositionServiceTest extends IntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(BybitPositionServiceTest.class);
    @Autowired
    BybitPositionService bybitPositionService;

    @Test
    @DisplayName("Get bybit position info")
    void getPositionInfo() {
        PositionVo expected = bybitPositionService.getPositionInfo();
        assertNotEquals(PositionVo.newInstance(), expected);
    }

    @Test
    @DisplayName("Get closed pnl list")
    void getClosedPnL() {
        BigDecimal expected = bybitPositionService.getClosedPnL();
        assertNotEquals(null, expected);
    }

}