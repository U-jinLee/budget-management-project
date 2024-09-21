package com.example.budget.domain.trade.service;

import com.bybit.api.client.domain.account.AccountType;
import com.bybit.api.client.domain.account.request.AccountDataRequest;
import com.bybit.api.client.restApi.BybitApiAccountRestClient;
import com.example.budget.domain.trade.model.AccountInfoVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
                .build();

        AccountInfoVo accountInfo = AccountInfoVo.newInstance();

        try {

            String jsonString =
                    new ObjectMapper().writeValueAsString(bybitApiAccountRestClient.getWalletBalance(request));

            log.info(jsonString);

            BigDecimal balance = new Gson().fromJson(jsonString, JsonObject.class)
                    .getAsJsonObject("result")
                    .getAsJsonArray("list")
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonArray("coin")
                    .get(3)
                    .getAsJsonObject()
                    .get("availableToWithdraw")
                    .getAsBigDecimal();

            accountInfo = new AccountInfoVo(balance);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accountInfo;
    }

}
