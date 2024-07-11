package com.example.budget.domain.investment.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.client.dto.TokenDto;
import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.investment.dto.StockDto;
import com.example.budget.global.jwt.JwtProvider;
import com.example.budget.global.setup.ClientSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StockApiControllerTest extends IntegrationTest {

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    ClientSetup clientSetup;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Post stock test")
    void postStock() throws Exception {

        //given
        StockDto.Request request =
                new StockDto.Request("HSBC(ADR)", "HSBC", 7.0f, 4191.0f);

        Client client = clientSetup.save();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(client.getEmail(), client.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtProvider.generateTokens(authentication);

        //when
        mvc.perform(post("/api/investment/stocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenDto.getAccessToken())
                        .content(objectMapper.writeValueAsString(request)))
                //then
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }
}