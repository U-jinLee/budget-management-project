package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.market.MarketInterval;
import com.example.budget.IntegrationTest;
import com.example.budget.domain.trade.dto.KlineDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        assertNotEquals(null, marketDataService.getMarkPrice());
    }
}