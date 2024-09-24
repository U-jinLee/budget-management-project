package com.example.budget.domain.trade.service;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.trade.model.PositionVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class BybitPositionServiceTest extends IntegrationTest {

    @Autowired
    BybitPositionService bybitPositionService;

    @Test
    @DisplayName("Get bybit position info")
    void getPositionInfo() {
        PositionVo result = bybitPositionService.getPositionInfo();
        assertNotEquals(PositionVo.newInstance(), result);
    }

}