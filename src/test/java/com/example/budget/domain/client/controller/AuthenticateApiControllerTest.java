package com.example.budget.domain.client.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.client.dto.SignInDto;
import com.example.budget.domain.client.dto.SignUpDto;
import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.client.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticateApiControllerTest extends IntegrationTest {

//    @Autowired
//    ClientRepository clientRepo;
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Test
//    void 회원_가입_성공() throws Exception {
//        //given
//        SignUpDto.Request request = SignUpDto.Request.from("yoojinlee.dev@gmail.com", "1q2w3e4r!");
//
//        mvc.perform(post("/api/authenticate/sign-up")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void 로그인_성공() throws Exception {
//
//        //given
//        String rawPassword = "1q2w3e4r!";
//        Client account = clientRepo.save(
//                Client.builder()
//                        .email("yoojinlee.dev@gmail.com")
//                        .password(passwordEncoder.encode(rawPassword))
//                        .build());
//
//        SignInDto.Request request = SignInDto.Request.from(account.getEmail(), rawPassword);
//
//        mvc.perform(get("/api/authenticate/sign-in")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
}