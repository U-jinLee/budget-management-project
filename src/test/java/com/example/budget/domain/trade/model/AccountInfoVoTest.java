package com.example.budget.domain.trade.model;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.trade.service.BybitAccountService;
import com.example.budget.domain.trade.service.MarketDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AccountInfoVoTest extends IntegrationTest {

    @Autowired
    BybitAccountService accountService;
    @Autowired
    MarketDataService marketDataService;

    @Test
    @DisplayName("Calculate order quantity")
    void calculateOrderQuantity() {
        AccountInfoVo accountInfo = accountService.getUSDTAvailableBalance();
        BigDecimal markPrice = marketDataService.getMarkPrice();

        BigDecimal orderQuantity =
                accountInfo.calculateOrderQuantity(BigDecimal.valueOf(0.125), markPrice);

        assertNotEquals(null, orderQuantity);
    }
}