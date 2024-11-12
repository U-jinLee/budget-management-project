package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.MarketInterval;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.example.budget.domain.trade.dto.KlineDto;
import com.example.budget.domain.trade.model.Coin;
import com.example.budget.domain.trade.model.Limit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MarketDataService {

    private final BybitApiMarketRestClient bybitApiMarketRestClient;

    public Map<MarketInterval, List<KlineDto>> getFuturesKlineLists(MarketInterval marketInterval1,
                                                                    MarketInterval marketInterval2) {
        Map<MarketInterval, List<KlineDto>> result = new EnumMap<>(MarketInterval.class);

        result.put(marketInterval1, getFuturesMarketLines(marketInterval1, true));
        result.put(marketInterval2, getFuturesMarketLines(marketInterval2, true));

        return result;
    }

    /**
     * Get market lines from bybit api
     *
     * @param marketInterval Check MarketInterval.enum class
     * @param fromOldest Choose whether to import late data
     * @return List<KlineDto>
     */
    public List<KlineDto> getFuturesMarketLines(MarketInterval marketInterval, boolean fromOldest) {
        MarketDataRequest request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .limit(Limit.TWO_HUNDRED.getValue())
                .marketInterval(marketInterval)
                .build();

        JsonArray jsonArray =
                new Gson()
                        .fromJson(bybitApiMarketRestClient.getMarketLinesData(request).toString(), JsonObject.class)
                        .getAsJsonObject("result")
                        .getAsJsonArray("list");

        List<KlineDto> result = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            result.add(KlineDto.newInstance(element.getAsJsonArray()));
        }

        if (fromOldest) Collections.reverse(result);

        return result;
    }

    public BigDecimal getMarkPrice() {
        MarketDataRequest request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(Coin.BTCUSDT.getValue())
                .build();

        var marketTickers = bybitApiMarketRestClient.getMarketTickers(request);

        BigDecimal markPrice = null;
        try {
            String result = new ObjectMapper().writeValueAsString(marketTickers);
            JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);

            markPrice = jsonObject.getAsJsonObject("result")
                    .getAsJsonArray("list")
                    .get(0).getAsJsonObject().get("markPrice").getAsBigDecimal();

        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }

        return markPrice;
    }

}
