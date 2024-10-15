package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.account.AccountType;
import com.bybit.api.client.domain.account.request.AccountDataRequest;
import com.bybit.api.client.restApi.BybitApiAccountRestClient;
import com.example.budget.domain.trade.model.AccountInfoVo;
import com.example.budget.global.util.JsonParsingUtil;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class BybitAccountService {

    private final BybitApiAccountRestClient bybitApiAccountRestClient;

    /**
     * Get my order available USDT balance
     *
     * @return My order available balance
     */
    public AccountInfoVo getUSDTAvailableBalance() {
        return new AccountInfoVo(getWalletBalance("USDT"));
    }

    /**
     * Get my order available USDC balance
     *
     * @return My order available balance
     */
    public AccountInfoVo getUSDCAvailableBalance() {
        return new AccountInfoVo(getWalletBalance("USDC"));
    }

    private BigDecimal getWalletBalance(String coin) {
        AccountDataRequest request = AccountDataRequest.builder()
                .accountType(AccountType.UNIFIED)
                .coins(coin)
                .build();

        JsonObject json = JsonParsingUtil.parsingToJson(bybitApiAccountRestClient.getWalletBalance(request));

        return json
                .getAsJsonObject()
                .getAsJsonArray("coin")
                .get(0)
                .getAsJsonObject()
                .get("availableToWithdraw")
                .getAsBigDecimal();
    }

}
