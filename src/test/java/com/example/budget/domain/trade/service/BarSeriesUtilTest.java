package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.market.MarketInterval;
import com.example.budget.IntegrationTest;
import com.example.budget.domain.trade.dto.KlineDto;
import com.example.budget.domain.trade.dto.RsiDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BarSeriesUtilTest extends IntegrationTest {

    @Autowired
    MarketDataService marketDataService;

    @Test
    @DisplayName("Rsi value 확인")
    void rsi() {
        List<KlineDto> klines = this.marketDataService
                .getFuturesMarketLines(MarketInterval.TWELVE_HOURLY, true);
        RsiDto rsi = new BarSeriesUtil(klines).rsi();
        RsiDto previousRsi = new BarSeriesUtil(klines).rsi(198);
        assertNotEquals(rsi, previousRsi);
    }

}