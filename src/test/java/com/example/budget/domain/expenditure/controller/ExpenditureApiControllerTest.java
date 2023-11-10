package com.example.budget.domain.expenditure.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.budget.repo.BudgetRepository;
import com.example.budget.domain.category.entity.Category;
import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.expenditure.dto.ExpenditurePostDto;
import com.example.budget.domain.expenditure.model.IsContain;
import com.example.budget.global.setup.BudgetSetup;
import com.example.budget.global.setup.CategorySetup;
import com.example.budget.global.setup.ClientSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.Assert.assertEquals;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExpenditureApiControllerTest extends IntegrationTest {
    @Autowired
    CategorySetup categorySetup;
    @Autowired
    ClientSetup clientSetup;
    @Autowired
    BudgetSetup budgetSetup;
    @Autowired
    BudgetRepository budgetRepo;

    @Test
    void 지출_넣기_성공() throws Exception {
        //given
        Category category = categorySetup.save();
        Client client = clientSetup.save();
        Budget budget = budgetSetup.save(category.getName(), client.getEmail());

        ExpenditurePostDto.Request request = ExpenditurePostDto.Request.from(100000L, "test");

        //when
        mvc.perform(post("/api/budgets/{budgetId}/expenditures", budget.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(request.getAmount()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.isContain").value(IsContain.CONTAIN.toString()));

        Budget changeBudget = budgetRepo.findById(budget.getId()).get();

        assertEquals(request.getAmount(), changeBudget.getAmountUsed());
    }

    @Test
    void putExpenditure() {
    }

    @Test
    void getExpenditures() {
    }

    @Test
    void getExpenditure() {
    }

    @Test
    void deleteExpenditure() {
    }

    @Test
    void excludeTotalExpenditure() {
    }
}