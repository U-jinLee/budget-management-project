package com.example.budget.domain.budget.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.budget.dto.BudgetPatchDto;
import com.example.budget.domain.budget.dto.BudgetPostDto;
import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.budget.repository.BudgetRepository;
import com.example.budget.domain.category.entity.Category;
import com.example.budget.domain.client.entity.Client;
import com.example.budget.global.setup.BudgetSetup;
import com.example.budget.global.setup.CategorySetup;
import com.example.budget.global.setup.ClientSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
    @Autowired
    BudgetRepository budgetRepo;

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


    @Test
    void 예산범위_수정_성공() throws Exception {
        //given
        Category category = categorySetup.save();
        Client client = clientSetup.save();
        Budget budget = budgetSetup.save(category.getName(), client.getEmail());
        assertEquals(1000000L, budget.getAmount());
        //when
        long changeAmount = 2000000L;
        mvc.perform(patch("/api/budgets/{id}", budget.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BudgetPatchDto.Request.from(changeAmount))))
                //then
                .andDo(print())
                .andExpect(status().isNoContent());
        Long changeBudget = budgetRepo.findById(budget.getId()).get().getAmount();
        assertEquals(changeAmount, changeBudget);
    }
}