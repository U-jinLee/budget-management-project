package com.example.budget.domain.client.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.domain.budget.entity.Budget;
import com.example.budget.domain.category.entity.Category;
import com.example.budget.domain.client.dto.SignUpDto;
import com.example.budget.domain.client.entity.Client;
import com.example.budget.global.setup.BudgetSetup;
import com.example.budget.global.setup.CategorySetup;
import com.example.budget.global.setup.ClientSetup;
import com.example.budget.global.setup.ExpenditureSetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ClientApiControllerTest extends IntegrationTest {

    @Autowired
    ClientSetup clientSetup;
    @Autowired
    CategorySetup categorySetup;
    @Autowired
    ExpenditureSetup expenditureSetup;
    @Autowired
    BudgetSetup budgetSetup;

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

    @Test
    void 금일_사용_가능_금액_안내_성공() throws Exception {
        //given
        Client client = clientSetup.save();

        Category category1 = categorySetup.save("주식");
        Category category2 = categorySetup.save("식비");

        Budget budget = budgetSetup.save(500000L, 275000L, category1.getName(), client.getEmail());
        Budget budget2 = budgetSetup.save(300000L, 650000L, category2.getName(), client.getEmail());

        expenditureSetup.save(100000L, budget);
        expenditureSetup.save(175000L, budget);

        expenditureSetup.save(100000L, budget2);
        expenditureSetup.save(5000L, budget2);

        //when
        mvc.perform(get("/api/clients/{clientId}/budgets/guide", client.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isOk());
    }

    @Test
    void 금일_사용_가능_금액_추천_성공() throws Exception {
        //given
        Client client = clientSetup.save();

        Category category1 = categorySetup.save("주식");
        Category category2 = categorySetup.save("식비");
        Category category3 = categorySetup.save("주거");

        Budget budget = budgetSetup.save(500000L, 275000L, category1.getName(), client.getEmail());
        Budget budget2 = budgetSetup.save(300000L, 650000L, category2.getName(), client.getEmail());
        Budget budget3 = budgetSetup.save(800000L, 50000L, category3.getName(), client.getEmail());

        expenditureSetup.save(100000L, budget);
        expenditureSetup.save(175000L, budget);

        expenditureSetup.save(100000L, budget2);
        expenditureSetup.save(5000L, budget2);

        //when
        mvc.perform(get("/api/clients/{clientId}/budgets/recommend", client.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isOk());
    }
}