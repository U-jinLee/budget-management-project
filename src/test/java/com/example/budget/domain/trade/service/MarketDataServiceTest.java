package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.market.MarketInterval;
import com.example.budget.IntegrationTest;
import com.example.budget.domain.trade.dto.KlineDto;
import com.example.budget.domain.trade.model.BybitAttributes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MarketDataServiceTest extends IntegrationTest {
    @Autowired
    MarketDataService marketDataService;

    @Test
    @DisplayName("Get local date time compare")
    void marketTimeCompare() {
        List<KlineDto> result =
                marketDataService.getFuturesMarketLines(MarketInterval.TWELVE_HOURLY, true);
        assertEquals(true, result.get(0).getLocalDateTime().isBefore(result.get(199).getLocalDateTime()));
    }

    @Test
    @DisplayName("Get 200 data from bybit")
    void getFuturesMarketLines() {
        List<KlineDto> result = marketDataService.getFuturesMarketLines(MarketInterval.TWELVE_HOURLY, true);
        assertEquals(200, result.size());
    }

    @Test
    @DisplayName("Get BTCUSDT mark price")
    void getMarkPrice() {
        BigDecimal markPrice = marketDataService.getMarkPrice();
        BigDecimal stopLoss = markPrice.multiply(BigDecimal.ONE.subtract(BybitAttributes.STOP_LOSS_PERCENTAGE))
                .setScale(2, RoundingMode.HALF_UP);
        assertEquals(1, markPrice.compareTo(stopLoss));
        assertNotEquals(null, markPrice);
    }
}