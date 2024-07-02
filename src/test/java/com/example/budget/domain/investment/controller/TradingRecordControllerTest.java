package com.example.budget.domain.investment.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.investment.dto.TradingRecordDto;
import com.example.budget.domain.investment.entity.Currency;
import com.example.budget.domain.investment.entity.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TradingRecordControllerTest extends IntegrationTest {

    @Test
    @DisplayName("Post trading record test")
    void postTradingRecord() throws Exception {
        //given
        TradingRecordDto.Request request =
                new TradingRecordDto.Request("애플",
                        "APPL",
                        "반도체 수혜로 인한 매매",
                        LocalDateTime.now(),
                        123.00f,
                        1.0f,
                        Position.BUY,
                        Currency.USD);

        //when
        mvc.perform(post("/api/investment/trading-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
        //then
                .andDo(print())
                .andExpect(status().isCreated());
    }

}