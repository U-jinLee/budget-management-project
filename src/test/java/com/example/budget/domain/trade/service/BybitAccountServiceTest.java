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
        System.out.println(balance);
        assertNotEquals(null, balance);
    }

    @Test
    @DisplayName("Get bybit USDCAvailableBalance")
    void getUSDCAvailableBalance() {
        AccountInfoVo balance = bybitAccountService.getUSDCAvailableBalance();
        System.out.println(balance);
        assertNotEquals(null, balance);
    }
}