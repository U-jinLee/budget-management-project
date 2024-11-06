package com.example.budget.domain.trade.service;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.trade.model.AccountInfoVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class BybitAccountServiceTest extends IntegrationTest {

    @Autowired
    BybitAccountService bybitAccountService;

    @Test
    @DisplayName("Get bybit USDTAvailableBalance")
    void getAvailableBalance() {
        AccountInfoVo balance = bybitAccountService.getUSDTAvailableBalance();
        assertNotEquals(null, balance);
    }

}