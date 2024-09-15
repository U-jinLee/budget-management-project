package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.market.MarketInterval;
import com.example.budget.IntegrationTest;
import com.example.budget.domain.trade.dto.KlineDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MarketDataServiceTest extends IntegrationTest {
    @Autowired
    MarketDataService marketDataService;

    @Test
    @DisplayName("Get 200 data from bybit")
    void getFuturesMarketLines() {
        List<KlineDto> result = marketDataService.getFuturesMarketLines(MarketInterval.TWELVE_HOURLY, true);
        assertEquals(200, result.size());
    }

}