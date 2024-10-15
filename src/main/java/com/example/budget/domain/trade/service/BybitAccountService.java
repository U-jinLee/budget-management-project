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
     * Get my order available balance
     *
     * @return My order available balance
     */
    public AccountInfoVo getUSDTAvailableBalance() {

        AccountDataRequest request = AccountDataRequest.builder()
                .accountType(AccountType.UNIFIED)
                .coins("USDT")
                .build();

        JsonObject json = JsonParsingUtil.parsingToJson(bybitApiAccountRestClient.getWalletBalance(request));

        BigDecimal balance = json
                .getAsJsonObject()
                .getAsJsonArray("coin")
                .get(0)
                .getAsJsonObject()
                .get("availableToWithdraw")
                .getAsBigDecimal();

        return new AccountInfoVo(balance);
    }

}
