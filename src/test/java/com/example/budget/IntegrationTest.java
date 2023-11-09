package com.example.budget;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BudgetManagementApplication.class)
@AutoConfigureMockMvc
@Transactional
public abstract class IntegrationTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper objectMapper;
}
