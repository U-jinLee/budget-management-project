package com.example.budget.global.util;

import com.bybit.api.client.domain.market.MarketInterval;
import com.example.budget.IntegrationTest;
import com.example.budget.domain.trade.dto.KlineDto;
import com.example.budget.domain.trade.service.MarketDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class ShortSafetyPositionUtilTest extends IntegrationTest {

    @Autowired
    MarketDataService marketDataService;
    @Test
    void isPositiveClose() {
        List<KlineDto> klines =
                this.marketDataService.getFuturesMarketLines(MarketInterval.TWELVE_HOURLY, true);
        ShortSafetyPositionUtil.isPositiveClose(klines);
    }
}