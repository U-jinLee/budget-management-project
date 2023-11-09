package com.example.budget.domain.budget.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.budget.dto.BudgetPostDto;
import com.example.budget.domain.category.entity.Category;
import com.example.budget.domain.client.entity.Client;
import com.example.budget.global.setup.BudgetSetup;
import com.example.budget.global.setup.CategorySetup;
import com.example.budget.global.setup.ClientSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BudgetApiControllerTest extends IntegrationTest {

    @Autowired
    CategorySetup categorySetup;
    @Autowired
    ClientSetup clientSetup;
    @Autowired
    BudgetSetup budgetSetup;
    @Test
    void 예산_설정_성공() throws Exception {
        //given
        Category category = categorySetup.save();
        Client client = clientSetup.save();
        //when
        BudgetPostDto.Request request =
                BudgetPostDto.Request.from(1000000L, client.getEmail(), category.getName());

        mvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void 중복_예산_설정_에러() throws Exception {
        //given
        Category category = categorySetup.save();
        Client client = clientSetup.save();
        budgetSetup.save(category.getName(), client.getEmail());

        //when
        BudgetPostDto.Request request =
                BudgetPostDto.Request.from(1000000L, client.getEmail(), category.getName());

        mvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }


}