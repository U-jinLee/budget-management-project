package com.example.budget.domain.expenditure.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.budget.repository.BudgetRepository;
import com.example.budget.domain.category.entity.Category;
import com.example.budget.domain.client.entity.Client;
import com.example.budget.domain.expenditure.dto.ExpenditurePostDto;
import com.example.budget.domain.expenditure.dto.ExpenditurePutDto;
import com.example.budget.domain.expenditure.entity.Expenditure;
import com.example.budget.domain.expenditure.model.IsContain;
import com.example.budget.domain.expenditure.repository.ExpenditureRepository;
import com.example.budget.global.setup.BudgetSetup;
import com.example.budget.global.setup.CategorySetup;
import com.example.budget.global.setup.ClientSetup;
import com.example.budget.global.setup.ExpenditureSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExpenditureApiControllerTest extends IntegrationTest {
//    @Autowired
//    CategorySetup categorySetup;
//    @Autowired
//    ClientSetup clientSetup;
//    @Autowired
//    BudgetSetup budgetSetup;
//    @Autowired
//    ExpenditureSetup expenditureSetup;
//
//    @Autowired
//    BudgetRepository budgetRepo;
//    @Autowired
//    ExpenditureRepository expenditureRepo;
//
//    @Test
//    void 지출_넣기_성공() throws Exception {
//        //given
//        Category category = categorySetup.save();
//        Client client = clientSetup.save();
//        Budget budget = budgetSetup.save(category.getName(), client.getEmail());
//
//        ExpenditurePostDto.Request request = ExpenditurePostDto.Request.from(5000L, "test");
//
//        //when
//        mvc.perform(post("/api/budgets/{budgetId}/expenditures", budget.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                //then
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.amount").value(request.getAmount()))
//                .andExpect(jsonPath("$.description").value(request.getDescription()))
//                .andExpect(jsonPath("$.isContain").value(IsContain.CONTAIN.toString()));
//
//        Budget changeBudget = budgetRepo.findById(budget.getId()).get();
//
//        assertEquals(request.getAmount(), changeBudget.getAmountUsed());
//    }
//
//    @Test
//    void 지출_내역_수정_성공() throws Exception {
//        //given
//        Category category = categorySetup.save();
//        Client client = clientSetup.save();
//        Budget budget = budgetSetup.save(category.getName(), client.getEmail());
//        Expenditure expenditure = expenditureSetup.save(budget);
//
//        ExpenditurePutDto.Request request = ExpenditurePutDto.Request.from(15000L, "changed test");
//
//        mvc.perform(put("/api/budgets/{budgetId}/expenditures/{id}", budget.getId(), expenditure.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//        Budget changedBudget = budgetRepo.findById(budget.getId()).get();
//        Expenditure changedExpenditure = expenditureRepo.findById(expenditure.getId()).get();
//
//        Assertions.assertEquals(15000L, changedExpenditure.getAmount());
//        Assertions.assertEquals(10000L, changedBudget.getAmountUsed());
//    }
//
//    @Test
//    void 지출_가져오기_성공() throws Exception {
//        //given
//        Category category = categorySetup.save();
//        Client client = clientSetup.save();
//        Budget budget = budgetSetup.save(category.getName(), client.getEmail());
//        expenditureSetup.save(budget);
//
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime afterSevenDay = now.plusDays(7);
//
//        //when
//        mvc.perform(get("/api/budgets/{budgetId}/expenditures", budget.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("start-date", now.toString())
//                        .param("end-date", afterSevenDay.toString()))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void 지출_카테고리_가져오기_성공() throws Exception {
//        //given
//        List<Category> categories = categorySetup.save(2);
//
//        Client client = clientSetup.save();
//        Budget budget = budgetSetup.save(categories.get(0).getName(), client.getEmail());
//        Budget secondBudget = budgetSetup.save(categories.get(1).getName(), client.getEmail());
//
//        expenditureSetup.save(budget);
//        expenditureSetup.save(secondBudget);
//
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime afterSevenDay = now.plusDays(7);
//
//        //when
//        mvc.perform(get("/api/budgets/{budgetId}/expenditures", budget.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("start-date", now.toString())
//                        .param("end-date", afterSevenDay.toString())
//                        .param("category", categories.get(0).getName()))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray());
//    }
//
//    @Test
//    void 지출_상세_가져오기_성공() throws Exception {
//        //given
//        Category category = categorySetup.save();
//        Client client = clientSetup.save();
//        Budget budget = budgetSetup.save(category.getName(), client.getEmail());
//        Expenditure expenditure = expenditureSetup.save(budget);
//        //when
//        mvc.perform(get("/api/budgets/{budgetId}/expenditures/{id}", budget.getId(), expenditure.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.amount").exists())
//                .andExpect(jsonPath("$.description").exists())
//                .andExpect(jsonPath("$.isContain").exists())
//                .andExpect(jsonPath("$.createdAt").exists());
//    }
//
//    @Test
//    void 지출_삭제_성공() throws Exception {
//        //given
//        Category category = categorySetup.save();
//        Client client = clientSetup.save();
//        Budget budget = budgetSetup.save(category.getName(), client.getEmail());
//        Expenditure expenditure = expenditureSetup.save(budget);
//        //when
//        mvc.perform(delete("/api/budgets/{budgetId}/expenditures/{id}", budget.getId(), expenditure.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void 지출_합산_제외_성공() throws Exception {
//        //given
//        Category category = categorySetup.save();
//        Client client = clientSetup.save();
//        Budget budget = budgetSetup.save(category.getName(), client.getEmail());
//        Expenditure expenditure = expenditureSetup.save(budget);
//        //when
//        mvc.perform(patch("/api/budgets/{budgetId}/expenditures/{id}/exclude-total", budget.getId(), expenditure.getId())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isNoContent());
//
//        Expenditure changedExpenditure = expenditureRepo.findById(expenditure.getId()).get();
//        assertEquals(IsContain.NOT_CONTAIN, changedExpenditure.getIsContain());
//    }
}