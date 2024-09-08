package com.example.budget.domain.trade.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionVoTest {

    @Test
    @DisplayName("Get roi from PositionVo")
    void getRoi() {
        PositionVo position =
                new PositionVo("BTCUDST", "BUY", BigDecimal.valueOf(4933.59594013),
                        BigDecimal.valueOf(7.05666),
                        BigDecimal.valueOf(0.224));

        BigDecimal expected = BigDecimal.valueOf(0.14);
        BigDecimal result = position.getRoi();

        assertEquals(expected, result);
    }

}