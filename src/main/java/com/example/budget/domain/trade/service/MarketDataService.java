package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.MarketInterval;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.example.budget.domain.trade.dto.KlineDto;
import com.example.budget.domain.trade.model.Coin;
import com.example.budget.domain.trade.model.Limit;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MarketDataService {

    private final BybitApiMarketRestClient bybitApiMarketRestClient;

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

}
