package com.example.budget.domain.investment.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.investment.dto.TradingRecordDto;
import com.example.budget.domain.investment.entity.Currency;
import com.example.budget.domain.investment.entity.Position;
import com.example.budget.domain.investment.entity.TradingRecord;
import com.example.budget.domain.investment.repository.TradingRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TradingRecordControllerTest extends IntegrationTest {

//    @Autowired
//    TradingRecordRepository tradingRecordRepository;
//
//    @BeforeEach
//    void beforeEach() {
//        TradingRecord entity = TradingRecord.builder()
//                .name("삼성전자")
//                .tradingReason("hbm 엔비디아 승인 기대감으로 인한 상승")
//                .tradingDate(LocalDateTime.now())
//                .tradingPrice(84000.0f)
//                .tradingQuantity(5.0f)
//                .currency(Currency.KRW)
//                .position(Position.BUY)
//                .build();
//
//        tradingRecordRepository.save(entity);
//    }
//
//    @Test
//    @DisplayName("Post trading record test")
//    void postTradingRecord() throws Exception {
//        //given
//        TradingRecordDto.Request request =
//                new TradingRecordDto.Request("애플",
//                        "APPL",
//                        "반도체 수혜로 인한 매매",
//                        LocalDateTime.now(),
//                        123.00f,
//                        2.0f,
//                        Position.BUY,
//                        Currency.USD);
//
//        //when
//        mvc.perform(post("/api/investment/trading-records")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//        //then
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(header().exists("Location"));
//    }
//
//    @Test
//    @DisplayName("Get trading records test")
//    void getTradingRecords() throws Exception {
//        //given
//        //when
//        mvc.perform(get("/api/investment/trading-records")
//                        .contentType(MediaType.APPLICATION_JSON))
//                //then
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

}