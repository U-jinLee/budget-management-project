package com.example.budget.domain.trade.service;

import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TradingApiSiteConfig {

    @Value("${bybit.key.test-api}")
    private String bybitApiKey;
    @Value("${bybit.key.test-secret}")
    private String bybitSecretKey;
    @Value("${bybit.domain}")
    private String domain;
    @Value("${bybit.leverage}")
    private String leverage;

    @Bean
    public BybitApiClientFactory bybitClient() {
        return BybitApiClientFactory.newInstance(bybitApiKey, bybitSecretKey, domain);
    }

    @Bean
    public BybitApiMarketRestClient getBybitApiMarketRestClient() {
        return BybitApiClientFactory.newInstance(bybitApiKey, bybitSecretKey, domain).newMarketDataRestClient();
    }

}
