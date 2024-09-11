package com.example.budget.domain.category.controller;

import com.example.budget.IntegrationTest;
import com.example.budget.global.setup.CategorySetup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryApiControllerTest extends IntegrationTest {

//    @Autowired
//    CategorySetup categorySetup;
//
//    @Test
//    void 카테고리_불러오기_성공() throws Exception {
//
//        //given
//        categorySetup.save(5);
//
//        //when
//        mvc.perform(get("/api/categories")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].id").exists())
//                .andExpect(jsonPath("$[0].name").exists())
//                .andExpect(jsonPath("$[0].description").exists());
//
//    }

}