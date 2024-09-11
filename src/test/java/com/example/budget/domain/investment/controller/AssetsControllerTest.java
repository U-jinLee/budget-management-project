package com.example.budget.domain.investment.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.investment.dto.InvestmentRatioDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AssetsControllerTest extends IntegrationTest {

//    @Test
//    @DisplayName("The 100 Minus Your Age Rule Get Test")
//    void getInvestmentRatio() throws Exception {
//        //given
//        int birthDate = 1993;
//        long totalAssets = 30000000L;
//        InvestmentRatioDto.Request request = new InvestmentRatioDto.Request(birthDate, totalAssets);
//        //when
//        mvc.perform(get("/api/investment/assets/investment-ratio")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
}