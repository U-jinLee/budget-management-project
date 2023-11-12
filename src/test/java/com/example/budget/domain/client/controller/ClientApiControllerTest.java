package com.example.budget.domain.client.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.client.dto.SignUpDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ClientApiControllerTest extends IntegrationTest {

    @Test
    void 회원가입_성공() throws Exception {
        //given
        SignUpDto.Request request =
                SignUpDto.Request.from("leeujin1029@naver.com", "1q2w3e4r!");
        //when
        mvc.perform(post("/api/clients/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                //then
                .andExpect(status().isCreated());
    }

}