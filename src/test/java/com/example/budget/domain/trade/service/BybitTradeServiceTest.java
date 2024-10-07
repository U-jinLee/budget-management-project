package com.example.budget.domain.trade.service;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.trade.model.PositionVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class BybitTradeServiceTest extends IntegrationTest {
    @Autowired
    BybitTradeService bybitTradeService;

    @Test
    void getOpenOrder() {
        PositionVo result = bybitTradeService.getOpenOrder();
        assertNotEquals(PositionVo.newInstance(), result);
    }
}