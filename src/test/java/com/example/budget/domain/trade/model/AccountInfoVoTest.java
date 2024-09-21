package com.example.budget.domain.trade.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountInfoVoTest {

    @Test
    @DisplayName("Calculate order quantity")
    void calculateOrderQuantity() {
        BigDecimal orderQuantity =
                new AccountInfoVo(BigDecimal.valueOf(5000)).calculateOrderQuantity(BigDecimal.valueOf(0.125),
                        BigDecimal.valueOf(45000));

        assertEquals(BigDecimal.valueOf(0.014), orderQuantity);
    }
}